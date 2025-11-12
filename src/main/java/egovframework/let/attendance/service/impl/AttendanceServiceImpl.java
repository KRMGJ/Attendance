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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class AttendanceServiceImpl implements AttendanceService {
	private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
	private static final LocalTime WORK_START = LocalTime.of(9, 0);
	private static final LocalTime WORK_END = LocalTime.of(18, 0);

	@Autowired
	private AttendanceDAO attendanceDAO;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * username으로 직원 정보 로드
	 * 
	 * @param username 사용자 이름(이메일)
	 * @return 직원 정보
	 */
	private Employee loadEmployeeByUsername(String username) {
		// username을 이메일로 사용 중이라는 전제
		return employeeRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없음: " + username));
	}

	/**
	 * 오늘 날짜의 00:00:00 시각을 Date 객체로 반환
	 */
	private Date getTodayDate() {
		LocalDateTime todayMidnight = LocalDateTime.now(ZONE).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return Date.from(todayMidnight.atZone(ZONE).toInstant());
	}

	/**
	 * 출근 처리
	 */
	@Override
	public String checkIn(String username) throws Exception {
		try {
			Employee emp = loadEmployeeByUsername(username);

			Date today = getTodayDate();
			if (attendanceRepository.existsByEmpIdAndWorkDate(emp.getId(), today)) {
				return "already_check_in";
			}

			Date now = new Date();
			LocalTime nowTime = now.toInstant().atZone(ZONE).toLocalTime();

			Attendance a = Attendance.builder().empId(emp.getId()).workDate(today).checkIn(now).build();

			// 지각 판정
			String status = nowTime.isAfter(WORK_START) ? LATE : PRESENT;
			a.setStatus(status);
			a.setOvertimeMinutes(0);

			attendanceRepository.save(a);
		} catch (Exception e) {
			log.error("Error during check-in for user {}: {}", username, e.getMessage());
			return "checkin_fail";
		}
		return "checkin_success";
	}

	/**
	 * 퇴근 처리
	 */
	@Override
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

			a.setCheckOut(now);

			// 조퇴 판정
			if (nowTime.isBefore(WORK_END)) {
				a.setStatus(EARLY_LEAVE);
			} else {
				// 연장근로 계산
				LocalDateTime endBase = LocalDateTime.ofInstant(today.toInstant(), ZONE).withHour(WORK_END.getHour())
						.withMinute(WORK_END.getMinute());
				LocalDateTime nowLocal = now.toInstant().atZone(ZONE).toLocalDateTime();
				long overtime = Duration.between(endBase, nowLocal).toMinutes();

				a.setOvertimeMinutes((int) Math.max(0, overtime));

				if (!LATE.equals(a.getStatus())) {
					a.setStatus(PRESENT);
				}
			}
			attendanceRepository.save(a);
		} catch (Exception e) {
			log.error("Error during check-out for user {}: {}", username, e.getMessage());
			return "checkout_fail";
		}
		return "checkout_success";
	}

	/**
	 * 오늘 출퇴근 정보 조회
	 */
	@Override
	@Transactional(readOnly = true)
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
					.build();
		} catch (Exception e) {
			log.error("Error fetching today's attendance for empId {}: {}", empId, e.getMessage());
		}
		return attendance;
	}

	/**
	 * 최근 출퇴근 정보 조회
	 */
	@Override
	@Transactional(readOnly = true)
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
		}
		return recent;
	}

	/**
	 * 나의 출퇴근 기록 조회
	 */
	@Override
	@Transactional(readOnly = true)
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
		}
		return attendanceList;
	}

	/**
	 * 관리자 출퇴근 기록 조회
	 */
	@Override
	@Transactional(readOnly = true)
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
		}
		return dtos;
	}

	/**
	 * 부서별 월간 보고서 조회
	 */
	@Override
	public List<MonthlyDeptReportDto> getMonthlyDeptReport(Date start, Date end) throws Exception {
		return attendanceDAO.selectMonthlyDeptReport(start, end);
	}
}
