package egovframework.let.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.entity.Attendance;

public interface AttendanceRepositoryCustom {
	Page<Attendance> searchForAdmin(AdminAttendanceSearch cond, Pageable pageable);

	List<Attendance> findMyRange(String empId, LocalDate from, LocalDate to);
}
