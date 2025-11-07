package egovframework.let.attendance.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.let.attendance.dto.response.AttendanceViewDto;
import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.AttendanceRepository;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AttendanceServiceImpl implements AttendanceService {
	private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
	private static final LocalTime WORK_START = LocalTime.of(9, 0);
	private static final LocalTime WORK_END = LocalTime.of(18, 0);

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	private Employee loadEmployeeByUsername(String username) {
		// username을 이메일로 사용 중이라는 전제
		return employeeRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없음: " + username));
	}

	/**
	 * 출근 처리
	 */
	@Override
	public String checkIn(String username) {
		try {
			Employee emp = loadEmployeeByUsername(username);

			LocalDate today = LocalDate.now(ZONE);
			if (attendanceRepository.existsByEmpIdAndWorkDate(emp.getId(), today)) {
				return "already_check_in";
			}

			LocalDateTime now = LocalDateTime.now(ZONE);

			Attendance a = Attendance.builder().empId(emp.getId()).workDate(today).checkIn(now).build();

			// 지각 판정
			String status = now.toLocalTime().isAfter(WORK_START) ? "LATE" : "PRESENT";
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
	public String checkOut(String username) {
		try {
			Employee emp = loadEmployeeByUsername(username);

			LocalDate today = LocalDate.now(ZONE);
			Attendance a = attendanceRepository.findByEmpIdAndWorkDate(emp.getId(), today)
					.orElseThrow(() -> new IllegalStateException("출근 기록 없음"));

			if (a.getCheckOut() != null) {
				return "already_check_out";
			}

			LocalDateTime now = LocalDateTime.now(ZONE);
			a.setCheckOut(now);

			// 조퇴 판정
			if (now.toLocalTime().isBefore(WORK_END)) {
				a.setStatus("EARLY_LEAVE");
			} else {
				// 연장근로 계산
				LocalDateTime endBase = LocalDateTime.of(today, WORK_END);
				long overtime = Duration.between(endBase, now).toMinutes();
				a.setOvertimeMinutes((int) Math.max(0, overtime));
				if (!"LATE".equals(a.getStatus())) {
					a.setStatus("PRESENT");
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
	public AttendanceViewDto getToday(String empId) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate today = LocalDate.now(ZONE);
		AttendanceViewDto attendance = null;
		try {
			Attendance att = attendanceRepository.findByEmpIdAndWorkDate(empId, today).orElseGet(() -> {
				Attendance empty = Attendance.builder().empId(empId).workDate(today).status("NONE").build();
				return empty;
			});
			attendance = AttendanceViewDto.builder().workDate(att.getWorkDate().toString())
					.checkIn(att.getCheckIn() != null ? att.getCheckIn().format(fmt) : null)
					.checkOut(att.getCheckOut() != null ? att.getCheckOut().format(fmt) : null).status(att.getStatus())
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
	public List<AttendanceViewDto> getRecent(String empId) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		List<AttendanceViewDto> recent = null;
		try {
			List<Attendance> att = attendanceRepository.findTop7ByEmpIdOrderByWorkDateDesc(empId);
			recent = att.stream()
					.map(a -> AttendanceViewDto.builder().workDate(a.getWorkDate().toString())
							.checkIn(a.getCheckIn() != null ? a.getCheckIn().format(fmt) : null)
							.checkOut(a.getCheckOut() != null ? a.getCheckOut().format(fmt) : null)
							.status(a.getStatus()).build())
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Error fetching recent attendance for empId {}: {}", empId, e.getMessage());
		}
		return recent;
	}

}
