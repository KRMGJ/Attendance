package egovframework.let.attendance.service;

import org.springframework.data.domain.Page;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;

public interface AdminService {
	/**
	 * 관리자 출퇴근 기록 조회
	 * 
	 * @param cond 검색 조건
	 * @return 출퇴근 기록 페이지
	 */
	Page<AttendanceListDto> list(AdminAttendanceSearch cond);
}
