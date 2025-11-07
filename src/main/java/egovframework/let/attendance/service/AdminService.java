package egovframework.let.attendance.service;

import org.springframework.data.domain.Page;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;

public interface AdminService {
	Page<AttendanceListDto> list(AdminAttendanceSearch cond);
}
