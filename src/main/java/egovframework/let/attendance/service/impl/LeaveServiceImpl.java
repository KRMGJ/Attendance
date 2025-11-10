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
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import egovframework.let.attendance.repository.mybatis.LeaveRequestDAO;
import egovframework.let.attendance.service.LeaveService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService {
	@Autowired
	private LeaveRequestDAO leaveRequestDAO;

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
	private int calcDays(Date start, Date end) {
		LocalDate s = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate e = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return (int) (ChronoUnit.DAYS.between(s, e) + 1);
	}

	/**
	 * 남은 휴가 일수 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public LeaveBalance getRemaining(String empId) {
		int currentYear = Year.now().getValue();
		Optional<LeaveBalance> leaveBalance = null;
		try {
			leaveBalance = leaveBalanceRepository.findByEmpIdAndYear(empId, currentYear);
		} catch (Exception e) {
			log.error("Error fetching leave balance for empId {}: {}", empId, e.getMessage());
		}
		return leaveBalance
				.orElseGet(() -> LeaveBalance.builder().empId(empId).year(currentYear).total(0).used(0).build());
	}

	/**
	 * 휴가 신청
	 */
	@Override
	@Transactional
	public String request(String userEmail, NewLeaveDto dto) {
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
	public List<LeaveRequestListDto> myRequests(String userEmail) {
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
			log.error("Error fetching leave requests for userEmail {}: {}", userEmail, e.getMessage());
		}
		return requests;
	}

	/**
	 * 휴가 신청 승인
	 */
	@Override
	public void approve(String id, String approverUsername) {
		LeaveRequest lr = leaveRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("신청이 존재하지 않습니다."));
		if (!PENDING.equals(lr.getStatus())) {
			throw new IllegalStateException("승인할 수 없는 상태입니다.");
		}

		// 연차면 해당 연도 잔액 차감
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
	}

	/**
	 * 휴가 신청 거절
	 */
	@Override
	public void reject(String id, String approverUsername) {
		LeaveRequest lr = leaveRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("신청이 존재하지 않습니다."));
		if (!PENDING.equals(lr.getStatus())) {
			throw new IllegalStateException("반려할 수 없는 상태입니다.");
		}
		lr.setStatus(REJECTED);
		lr.setApprover(approverUsername);
		lr.setApprovedAt(new Date());
		leaveRequestRepository.save(lr);
	}

	/**
	 * 휴가 신청 취소
	 */
	@Override
	public void cancel(String id, String empId) {
		LeaveRequest lr = leaveRequestRepository.findByIdAndEmpId(id, empId)
				.orElseThrow(() -> new IllegalArgumentException("해당 신청이 존재하지 않습니다."));
		if (!PENDING.equals(lr.getStatus())) {
			throw new IllegalStateException("취소할 수 없는 상태입니다.");
		}
		lr.setStatus(CANCELED);
		lr.setApprover(null);
		lr.setApprovedAt(null);
		leaveRequestRepository.save(lr);
	}

}
