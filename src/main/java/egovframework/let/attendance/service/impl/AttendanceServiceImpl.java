package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Utils.formatDate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
	@Transactional(readOnly = true)
	public AttendanceViewDto getToday(String empId) {
		LocalDate today = LocalDate.now(ZONE);
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
	public List<AttendanceViewDto> getRecent(String empId) {
		List<AttendanceViewDto> recent = null;
		try {
			List<Attendance> att = attendanceRepository.findTop7ByEmpIdOrderByWorkDateDesc(empId);
			recent = att.stream()
					.map(a -> AttendanceViewDto.builder().workDate(a.getWorkDate().toString())
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
	public List<AttendanceListDto> getMyAttendance(String userEmail, LocalDate from, LocalDate to) {
		List<AttendanceListDto> attendanceList = null;
		try {
			Employee emp = loadEmployeeByUsername(userEmail);
			if (from != null || to != null) {
				List<Attendance> records = attendanceRepository.findMyRange(emp.getId(), from, to);
				attendanceList = records.stream()
						.map(a -> AttendanceListDto.builder().id(a.getId()).empId(a.getEmpId())
								.workDate(a.getWorkDate()).checkIn(formatDate(a.getCheckIn()))
								.checkOut(a.getCheckOut() != null ? formatDate(a.getCheckOut()) : null)
								.status(a.getStatus()).overtimeMinutes(a.getOvertimeMinutes()).employee(emp).build())
						.collect(Collectors.toList());
			} else {
				List<Attendance> records = attendanceRepository.findTop100ByEmpIdOrderByWorkDateDesc(emp.getId());
				attendanceList = records.stream()
						.map(a -> AttendanceListDto.builder().id(a.getId()).empId(a.getEmpId())
								.workDate(a.getWorkDate()).checkIn(formatDate(a.getCheckIn()))
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
	public Page<AttendanceListDto> list(AdminAttendanceSearch cond) {
		Page<AttendanceListDto> dtos = null;
		try {
			Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
			Page<Attendance> result = attendanceRepository.searchForAdmin(cond, pageable);
			dtos = result.map(attendance -> AttendanceListDto.builder().id(attendance.getId())
					.empId(attendance.getEmpId()).workDate(attendance.getWorkDate())
					.checkIn(formatDate(attendance.getCheckIn())).checkOut(formatDate(attendance.getCheckOut()))
					.status(attendance.getStatus()).overtimeMinutes(attendance.getOvertimeMinutes())
					.employee(attendance.getEmployee()).build());
		} catch (Exception e) {
			log.error("Error fetching attendance list for admin", e);
		}
		return dtos;
	}

}
