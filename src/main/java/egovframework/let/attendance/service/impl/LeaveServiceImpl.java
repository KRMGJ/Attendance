package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Enums.ANNUAL;
import static egovframework.let.attendance.common.Enums.APPROVED;
import static egovframework.let.attendance.common.Enums.CANCELED;
import static egovframework.let.attendance.common.Enums.PENDING;
import static egovframework.let.attendance.common.Enums.REJECTED;
import static egovframework.let.attendance.common.Enums.SICK;
import static egovframework.let.attendance.common.Utils.formatDateOnly;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.NewLeaveDto;
import egovframework.let.attendance.dto.response.LeaveRequestListDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.entity.LeaveBalance;
import egovframework.let.attendance.entity.LeaveRequest;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.repository.LeaveBalanceRepository;
import egovframework.let.attendance.repository.LeaveRequestRepository;
import egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO;
import egovframework.let.attendance.repository.mybatis.LeaveRequestDAO;
import egovframework.let.attendance.service.LeaveService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService {
	@Autowired
	private LeaveRequestDAO leaveRequestDAO;

	@Autowired
	private LeaveBalanceDAO leaveBalanceDAO;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	/**
	 * 시작일과 종료일 사이의 일수 계산 (양 끝일 포함)
	 * 
	 * @param start 시작일
	 * @param end   종료일
	 */
	private int calcDays(Date start, Date end) throws Exception {
		LocalDate s = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate e = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return (int) (ChronoUnit.DAYS.between(s, e) + 1);
	}

	/**
	 * 남은 휴가 일수 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public LeaveBalance getRemaining(String empId) throws Exception {
		int currentYear = Year.now().getValue();
		Optional<LeaveBalance> leaveBalance = null;
		try {
			leaveBalance = leaveBalanceRepository.findByEmpIdAndYear(empId, currentYear);
		} catch (Exception e) {
			log.error("Error fetching leave balance for empId {}: {}", empId, e);
			throw e;
		}
		return leaveBalance
				.orElseGet(() -> LeaveBalance.builder().empId(empId).year(currentYear).total(0).used(0).build());
	}

	/**
	 * 휴가 신청
	 */
	@Override
	@Transactional
	public String request(String userEmail, NewLeaveDto dto) throws Exception {
		try {
			Employee emp = employeeRepository.findByEmail(userEmail)
					.orElseThrow(() -> new IllegalStateException("직원 정보 없음"));

			Date start = dto.getStartDate();
			Date end = dto.getEndDate();
			if (start == null || end == null || end.before(start)) {
				throw new IllegalArgumentException("기간이 올바르지 않습니다.");
			}

			if (leaveRequestDAO.countOverlap(emp.getId(), start, end) > 0) {
				throw new IllegalStateException("이미 신청된 휴가와 기간이 겹칩니다.");
			}

			int days = calcDays(start, end);

			if (dto.getType() == ANNUAL) {
				int year = start.toInstant().atZone(ZoneId.systemDefault()).getYear();
				LeaveBalance bal = leaveBalanceRepository.findByEmpIdAndYear(emp.getId(), year)
						.orElseThrow(() -> new IllegalStateException("휴가 잔액 정보 없음"));
				if (bal.getRemaining() < days) {
					throw new IllegalStateException("연차 잔액 부족");
				}
			}

			String type = ANNUAL.equals(dto.getType()) ? ANNUAL : SICK;

			LeaveRequest req = LeaveRequest.builder().empId(emp.getId()).type(type).startDate(start).endDate(end)
					.days(days).reason(dto.getReason()).status(PENDING).build();

			leaveRequestRepository.save(req);
			return "휴가 신청이 완료되었습니다.";
		} catch (Exception e) {
			log.error("Error processing leave request for userEmail {}: {}", userEmail, e.getMessage());
			return "휴가 신청 중 오류가 발생했습니다.";
		}
	}

	/**
	 * 나의 휴가 신청 내역 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<LeaveRequestListDto> myRequests(String userEmail) throws Exception {
		List<LeaveRequestListDto> requests = null;
		try {
			Employee emp = employeeRepository.findByEmail(userEmail)
					.orElseThrow(() -> new IllegalStateException("직원 정보 없음"));

			List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmpIdOrderByIdDesc(emp.getId());

			requests = leaveRequests.stream().map(lr -> {
				LeaveRequestListDto dto = new LeaveRequestListDto();
				dto.setType(lr.getType());
				dto.setStartDate(formatDateOnly(lr.getStartDate()));
				dto.setEndDate(formatDateOnly(lr.getEndDate()));
				dto.setDays(lr.getDays());
				dto.setReason(lr.getReason());
				dto.setStatus(lr.getStatus());
				dto.setCreatedAt(lr.getCreatedAt());
				dto.setApprover(lr.getApprover());
				dto.setApprovedAt(lr.getApprovedAt() != null ? formatDateOnly(lr.getApprovedAt()) : null);
				return dto;
			}).collect(Collectors.toList());

		} catch (Exception e) {
			log.error("Error fetching leave requests for userEmail {}: {}", userEmail, e);
			throw e;
		}
		return requests;
	}

	/**
	 * 휴가 신청 승인
	 */
	@Override
	public void approve(String id, String approverUsername) throws Exception {
		try {
			LeaveRequest lr = leaveRequestRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("신청이 존재하지 않습니다."));
			if (!PENDING.equals(lr.getStatus())) {
				throw new IllegalStateException("승인할 수 없는 상태입니다.");
			}

			if (lr.getType() == ANNUAL) {
				int year = lr.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear();
				LeaveBalance bal = leaveBalanceRepository.findByEmpIdAndYear(lr.getEmpId(), year)
						.orElseThrow(() -> new IllegalStateException("휴가 잔액 정보 없음"));
				if (bal.getRemaining() < lr.getDays()) {
					throw new IllegalStateException("연차 잔액 부족");
				}
				bal.setUsed(bal.getUsed() + lr.getDays());
				leaveBalanceRepository.save(bal);
			}

			lr.setStatus(APPROVED);
			lr.setApprover(approverUsername);
			lr.setApprovedAt(new Date());
			leaveRequestRepository.save(lr);
		} catch (Exception e) {
			log.error("Error approving leave request ID {}: {}", id, e);
			throw e;
		}
	}

	/**
	 * 휴가 신청 거절
	 */
	@Override
	public void reject(String id, String approverUsername) throws Exception {
		try {
			LeaveRequest lr = leaveRequestRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("신청이 존재하지 않습니다."));
			if (!PENDING.equals(lr.getStatus())) {
				throw new IllegalStateException("반려할 수 없는 상태입니다.");
			}
			lr.setStatus(REJECTED);
			lr.setApprover(approverUsername);
			lr.setApprovedAt(new Date());
			leaveRequestRepository.save(lr);
		} catch (Exception e) {
			log.error("Error rejecting leave request ID {}: {}", id, e);
			throw e;
		}
	}

	/**
	 * 휴가 신청 취소
	 */
	@Override
	public void cancel(String id, String empId) throws Exception {
		try {
			LeaveRequest lr = leaveRequestRepository.findByIdAndEmpId(id, empId)
					.orElseThrow(() -> new IllegalArgumentException("해당 신청이 존재하지 않습니다."));
			if (!PENDING.equals(lr.getStatus())) {
				throw new IllegalStateException("취소할 수 없는 상태입니다.");
			}
			lr.setStatus(CANCELED);
			lr.setApprover(null);
			lr.setApprovedAt(null);
			leaveRequestRepository.save(lr);
		} catch (Exception e) {
			log.error("Error canceling leave request ID {} for empId {}: {}", id, empId, e);
			throw e;
		}
	}

	/**
	 * 전월 개근자에게 월 1일 연차 부여
	 */
	@Override
	public void grantMonthlyAccrualIfEligible(Date targetDate) throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		cal.setTime(targetDate);
		// 전월로 이동
		cal.add(Calendar.MONTH, -1);
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		try {
			// 1년 미만 + 전월 개근자
			List<String> empIds = leaveBalanceDAO.selectFirstYearFullAttendanceEmployees(y, m);
			for (String empId : empIds) {
				Integer year = y;
				String upsertId = UUID.randomUUID().toString();
				leaveBalanceDAO.upsertLeaveBalance(upsertId, empId, year);

				int monthlyGranted = leaveBalanceDAO.countMonthlyGranted(empId, year);
				if (monthlyGranted < 11) {
					leaveBalanceDAO.addQuota(empId, year, 1);
					String logId = UUID.randomUUID().toString();
					leaveBalanceDAO.insertGrantLog(logId, empId, year, "MONTHLY_1D", 1, new Date());
				}
			}
		} catch (Exception e) {
			log.error("Error granting monthly accrual for targetDate {}: {}", targetDate, e);
			throw e;
		}
	}

	/**
	 * 전월 월차 부여 중복 처리 방지
	 */
	@Override
	public void ensureLastMonthMonthlyAccrualClosed() throws Exception {
		try {
			leaveBalanceDAO.fixMonthlyAccrualDuplicate(new Date());
		} catch (Exception e) {
			log.error("Error ensuring last month monthly accrual closed: {}", e);
			throw e;
		}
	}

	/**
	 * 입사기준 연차 부여
	 */
	@Override
	public void grantAnnualByAnniversary(Date today) throws Exception {
		try {
			List<Map<String, Object>> targets = leaveBalanceDAO.selectEmployeesWithAnniversaryToday(today);
			for (Map<String, Object> row : targets) {
				String empId = (String) row.get("empId");
				Date hireDate = (Date) row.get("hireDate");
				int years = diffYears(hireDate, today);
				if (years < 1) {
					continue;
				}

				int base = 15;
				int extra = Math.max(0, (years - 1) / 2);
				int grant = Math.min(25, base + extra);

				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
				cal.setTime(today);
				int year = cal.get(Calendar.YEAR);

				String upsertId = UUID.randomUUID().toString();
				leaveBalanceDAO.upsertLeaveBalance(upsertId, empId, year);
				leaveBalanceDAO.setAnnualQuota(empId, year, grant);

				String logId = UUID.randomUUID().toString();
				leaveBalanceDAO.insertGrantLog(logId, empId, year, "ANNUAL_RESET", grant, today);
			}
		} catch (Exception e) {
			log.error("Error granting annual leave by anniversary for date {}: {}", today, e);
			throw e;
		}
	}

	/**
	 * 달력기준 연차 부여
	 */
	@Override
	public void grantAnnualByCalendarYear(int year) throws Exception {
		try {
			List<Map<String, Object>> emps = leaveBalanceDAO.selectActiveEmployeesOnYear(year);
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date ref = cal.getTime();

			for (Map<String, Object> row : emps) {
				String empId = (String) row.get("empId");
				Date hireDate = (Date) row.get("hireDate");
				int years = diffYears(hireDate, ref);
				if (years < 1) {
					continue;
				}

				int base = 15;
				int extra = Math.max(0, (years - 1) / 2);
				int grant = Math.min(25, base + extra);

				String upsertId = UUID.randomUUID().toString();
				leaveBalanceDAO.upsertLeaveBalance(upsertId, empId, year);
				leaveBalanceDAO.setAnnualQuota(empId, year, grant);

				String logId = UUID.randomUUID().toString();
				leaveBalanceDAO.insertGrantLog(logId, empId, year, "ANNUAL_RESET", grant, ref);
			}
		} catch (Exception e) {
			log.error("Error granting annual leave by calendar year {}: {}", year, e);
			throw e;
		}
	}

	/**
	 * 두 날짜 사이의 연도 차이 계산
	 * 
	 * @param from 시작 날짜
	 * @param to   종료 날짜
	 * @return 연도 차이
	 */
	private int diffYears(Date from, Date to) {
		Calendar a = Calendar.getInstance();
		a.setTime(from);
		Calendar b = Calendar.getInstance();
		b.setTime(to);
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (b.get(Calendar.MONTH) < a.get(Calendar.MONTH) || (b.get(Calendar.MONTH) == a.get(Calendar.MONTH)
				&& b.get(Calendar.DAY_OF_MONTH) < a.get(Calendar.DAY_OF_MONTH))) {
			diff--;
		}
		return diff;
	}

}
