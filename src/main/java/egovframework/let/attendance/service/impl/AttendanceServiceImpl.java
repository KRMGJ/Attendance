package egovframework.let.attendance.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.AttendanceRepository;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.AttendanceVO;
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

	@Override
	public List<AttendanceVO> selectAttendanceList(AttendanceVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

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

	private Employee loadEmployeeByUsername(String username) {
		// username을 이메일로 사용 중이라는 전제
		return employeeRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없음: " + username));
	}

}
