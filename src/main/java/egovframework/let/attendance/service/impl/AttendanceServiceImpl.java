package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Enums.EARLY_LEAVE;
import static egovframework.let.attendance.common.Enums.LATE;
import static egovframework.let.attendance.common.Enums.PRESENT;
import static egovframework.let.attendance.common.Utils.formatDate;
import static egovframework.let.attendance.common.Utils.formatDateOnly;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.dto.response.AttendanceViewDto;
import egovframework.let.attendance.dto.response.MonthlyDeptReportDto;
import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.AttendanceRepository;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.repository.mybatis.AttendanceDAO;
import egovframework.let.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("attendanceService")
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {
	private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
	private static final LocalTime WORK_START = LocalTime.of(9, 0);
	private static final LocalTime WORK_END = LocalTime.of(18, 0);

	@Resource(name = "attendanceDAO")
	private AttendanceDAO attendanceDAO;

	@Resource(name = "attendanceRepository")
	private AttendanceRepository attendanceRepository;

	@Resource(name = "employeeRepository")
	private EmployeeRepository employeeRepository;

	@Resource(name = "attendanceIdGnrService")
	private EgovIdGnrService attendanceIdGnrService;

	/**
	 * 출근 처리
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String checkIn(String username) throws Exception {
		try {
			Employee emp = loadEmployeeByUsername(username);

			Date today = getTodayDate();
			if (attendanceRepository.existsByEmpIdAndWorkDate(emp.getId(), today)) {
				return "already_check_in";
			}

			Date now = new Date();
			LocalTime nowTime = now.toInstant().atZone(ZONE).toLocalTime();

			// 직원별 기준 시작시간 계산
			LocalTime workStart = resolveWorkStart(emp);

			Attendance a = Attendance.builder().id(attendanceIdGnrService.getNextStringId()).empId(emp.getId())
					.workDate(today).checkIn(now).build();

			// 지각 판정: 직원별 기준시간 사용
			String status = nowTime.isAfter(workStart) ? LATE : PRESENT;
			a.setStatus(status);
			a.setOvertimeMinutes(0);

			attendanceRepository.save(a);
			return "checkin_success";
		} catch (Exception e) {
			log.error("Error during check-in for user {}: {}", username, e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "checkin_fail";
		}
	}

	/**
	 * 퇴근 처리
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String checkOut(String username) throws Exception {
		try {
			Employee emp = loadEmployeeByUsername(username);

			Date today = getTodayDate();
			Attendance a = attendanceRepository.findByEmpIdAndWorkDate(emp.getId(), today)
					.orElseThrow(() -> new IllegalStateException("출근 기록 없음"));

			if (a.getCheckOut() != null) {
				return "already_check_out";
			}

			Date now = new Date();
			LocalTime nowTime = now.toInstant().atZone(ZONE).toLocalTime();

			LocalTime workEnd = resolveWorkEnd(emp);

			a.setCheckOut(now);

			if (nowTime.isBefore(workEnd)) {
				a.setStatus(EARLY_LEAVE);
			} else {
				LocalDateTime endBase = LocalDateTime.ofInstant(today.toInstant(), ZONE).withHour(workEnd.getHour())
						.withMinute(workEnd.getMinute());

				LocalDateTime nowLocal = now.toInstant().atZone(ZONE).toLocalDateTime();
				long overtime = Duration.between(endBase, nowLocal).toMinutes();

				a.setOvertimeMinutes((int) Math.max(0, overtime));

				if (!LATE.equals(a.getStatus())) {
					a.setStatus(PRESENT);
				}
			}
			int workedMinutes = calcWorkedMinutes(a.getCheckIn(), now);
			a.setWorkedMinutes(workedMinutes);

			attendanceRepository.save(a);
			return "checkout_success";
		} catch (Exception e) {
			log.error("Error during check-out for user {}: {}", username, e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "checkout_fail";
		}
	}

	/**
	 * 오늘 출퇴근 정보 조회
	 */
	@Override
	public AttendanceViewDto getToday(String empId) throws Exception {
		Date today = getTodayDate();
		AttendanceViewDto attendance = null;
		try {
			Attendance att = attendanceRepository.findByEmpIdAndWorkDate(empId, today).orElseGet(() -> {
				Attendance empty = Attendance.builder().empId(empId).workDate(today).status("NONE").build();
				return empty;
			});
			attendance = AttendanceViewDto.builder().workDate(att.getWorkDate().toString())
					.checkIn(att.getCheckIn() != null ? formatDate(att.getCheckIn()) : null)
					.checkOut(att.getCheckOut() != null ? formatDate(att.getCheckOut()) : null).status(att.getStatus())
					.workedMinutes(att.getWorkedMinutes()).build();
		} catch (Exception e) {
			log.error("Error fetching today's attendance for empId {}: {}", empId, e.getMessage());
			throw e;
		}
		return attendance;
	}

	/**
	 * 최근 출퇴근 정보 조회
	 */
	@Override
	public List<AttendanceViewDto> getRecent(String empId) throws Exception {
		List<AttendanceViewDto> recent = null;
		try {
			List<Attendance> att = attendanceRepository.findTop7ByEmpIdOrderByWorkDateDesc(empId);
			recent = att.stream()
					.map(a -> AttendanceViewDto.builder().workDate(formatDateOnly(a.getWorkDate()))
							.checkIn(a.getCheckIn() != null ? formatDate(a.getCheckIn()) : null)
							.checkOut(a.getCheckOut() != null ? formatDate(a.getCheckOut()) : null)
							.status(a.getStatus()).build())
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error fetching recent attendance for empId {}: {}", empId, e.getMessage());
			throw e;
		}
		return recent;
	}

	/**
	 * 나의 출퇴근 기록 조회
	 */
	@Override
	public List<AttendanceListDto> getMyAttendance(String userEmail, Date from, Date to) throws Exception {
		List<AttendanceListDto> attendanceList = null;
		try {
			Employee emp = loadEmployeeByUsername(userEmail);
			if (from != null || to != null) {
				List<Attendance> records = attendanceDAO.findMyRange(emp.getId(), from, to);
				attendanceList = records.stream()
						.map(a -> AttendanceListDto.builder().id(a.getId()).empId(a.getEmpId())
								.workDate(formatDateOnly(a.getWorkDate())).checkIn(formatDate(a.getCheckIn()))
								.checkOut(a.getCheckOut() != null ? formatDate(a.getCheckOut()) : null)
								.status(a.getStatus()).overtimeMinutes(a.getOvertimeMinutes()).employee(emp).build())
						.collect(Collectors.toList());
			} else {
				List<Attendance> records = attendanceRepository.findTop100ByEmpIdOrderByWorkDateDesc(emp.getId());
				attendanceList = records.stream()
						.map(a -> AttendanceListDto.builder().id(a.getId()).empId(a.getEmpId())
								.workDate(formatDateOnly(a.getWorkDate())).checkIn(formatDate(a.getCheckIn()))
								.checkOut(a.getCheckOut() != null ? formatDate(a.getCheckOut()) : null)
								.status(a.getStatus()).overtimeMinutes(a.getOvertimeMinutes()).employee(emp).build())
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error("Error fetching attendance records for user {}: {}", userEmail, e.getMessage());
			throw e;
		}
		return attendanceList;
	}

	/**
	 * 관리자 출퇴근 기록 조회
	 */
	@Override
	public Page<AttendanceListDto> list(AdminAttendanceSearch cond) throws Exception {
		Page<AttendanceListDto> dtos = null;
		try {
			Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
			Page<Attendance> result = attendanceRepository.searchForAdmin(cond, pageable);
			dtos = result.map(attendance -> AttendanceListDto.builder().id(attendance.getId())
					.empId(attendance.getEmpId()).workDate(formatDateOnly(attendance.getWorkDate()))
					.checkIn(formatDate(attendance.getCheckIn())).checkOut(formatDate(attendance.getCheckOut()))
					.status(attendance.getStatus()).overtimeMinutes(attendance.getOvertimeMinutes())
					.employee(attendance.getEmployee()).build());
		} catch (Exception e) {
			log.error("Error fetching attendance list for admin", e);
			throw e;
		}
		return dtos;
	}

	/**
	 * 부서별 월간 보고서 조회
	 */
	@Override
	public List<MonthlyDeptReportDto> getMonthlyDeptReport(Date start, Date end) throws Exception {
		try {
			if (start == null || end == null) {
				throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다.");
			}
			return attendanceDAO.selectMonthlyDeptReport(start, end);
		} catch (Exception e) {
			log.error("Error validating dates for monthly department report: {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public int getWorkedMinutesByPeriod(String email, Date from, Date to) throws Exception {
		if (email == null || from == null || to == null) {
			return 0;
		}
		Employee emp = loadEmployeeByUsername(email);
		return attendanceDAO.sumWorkedMinutesByPeriod(emp.getId(), from, to);
	}

	/**
	 * email로 직원 정보 로드
	 * 
	 * @param email 직원 이메일
	 * @return 직원 정보
	 */
	private Employee loadEmployeeByUsername(String email) {
		return employeeRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없음: " + email));
	}

	/**
	 * 오늘 날짜의 00:00:00 시각을 Date 객체로 반환
	 * 
	 * @return 오늘 날짜의 Date 객체
	 */
	private Date getTodayDate() {
		LocalDateTime todayMidnight = LocalDateTime.now(ZONE).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return Date.from(todayMidnight.atZone(ZONE).toInstant());
	}

	/**
	 * 직원별 출근 시간 설정값 확인
	 * 
	 * @param emp 직원 정보
	 * @return 출근 시간
	 */
	private LocalTime resolveWorkStart(Employee emp) {
		if (emp.getWorkStartTime() != null && !emp.getWorkStartTime().isEmpty()) {
			return LocalTime.parse(emp.getWorkStartTime());
		}
		return WORK_START;
	}

	/**
	 * 직원별 퇴근 시간 설정값 확인
	 * 
	 * @param emp 직원 정보
	 * @return 퇴근 시간
	 */
	private LocalTime resolveWorkEnd(Employee emp) {
		if (emp.getWorkEndTime() != null && !emp.getWorkEndTime().isEmpty()) {
			return LocalTime.parse(emp.getWorkEndTime());
		}
		return WORK_END;
	}

	/**
	 * 출퇴근 시간으로 근무 시간(분 단위) 계산
	 * 
	 * @param checkIn  출근 시간
	 * @param checkOut 퇴근 시간
	 * @return 근무 시간(분 단위)
	 */
	private int calcWorkedMinutes(Date checkIn, Date checkOut) {
		if (checkIn == null || checkOut == null) {
			return 0;
		}
		long diffMillis = checkOut.getTime() - checkIn.getTime();
		if (diffMillis <= 0L) {
			return 0;
		}
		long minutes = diffMillis / (1000L * 60L);

		long lunchMinutes = 0L;
		if (checkIn.toInstant().atZone(ZONE).toLocalTime().isBefore(LocalTime.of(13, 0))
				&& checkOut.toInstant().atZone(ZONE).toLocalTime().isAfter(LocalTime.of(12, 0))) {
			lunchMinutes = 60L;
		}
		minutes -= lunchMinutes;

		return (int) minutes;
	}
}
