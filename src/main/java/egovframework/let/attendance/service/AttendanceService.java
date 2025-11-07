package egovframework.let.attendance.service;

import java.util.List;

import egovframework.let.attendance.dto.response.AttendanceViewDto;

public interface AttendanceService {
	List<AttendanceVO> selectAttendanceList(AttendanceVO vo);

	/**
	 * 출근 처리
	 * 
	 * @param username 사용자 이름(이메일)
	 * @return 성공여부
	 */
	String checkIn(String username);

	/**
	 * 퇴근 처리
	 * 
	 * @param username 사용자 이름(이메일)
	 * @return 성공여부
	 */
	String checkOut(String username);

	/**
	 * 오늘 출퇴근 정보 조회
	 * 
	 * @param empId 직원 ID
	 * @return 오늘 출퇴근 정보
	 */
	AttendanceViewDto getToday(String empId);

	/**
	 * 최근 출퇴근 정보 조회
	 * 
	 * @param empId 직원 ID
	 * @return 최근 출퇴근 정보 리스트
	 */
	List<AttendanceViewDto> getRecent(String empId);
}
