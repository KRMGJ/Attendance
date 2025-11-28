package egovframework.let.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.request.UserAttendanceSearch;
import egovframework.let.attendance.entity.Attendance;

public interface AttendanceRepositoryCustom {
	Page<Attendance> searchForAdmin(AdminAttendanceSearch cond, Pageable pageable);

	Page<Attendance> findMyRange(String empId, UserAttendanceSearch cond, Pageable pageable);
}
