package egovframework.let.attendance.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.dto.response.AttendanceViewDto;
import egovframework.let.attendance.dto.response.MonthlyDeptReportDto;

public interface AttendanceService {
	/**
	 * 출근 처리
	 * 
	 * @param username 사용자 이름(이메일)
	 * @return 성공여부
	 */
	String checkIn(String username) throws Exception;

	/**
	 * 퇴근 처리
	 * 
	 * @param username 사용자 이름(이메일)
	 * @return 성공여부
	 */
	String checkOut(String username) throws Exception;

	/**
	 * 오늘 출퇴근 정보 조회
	 * 
	 * @param empId 직원 ID
	 * @return 오늘 출퇴근 정보
	 */
	AttendanceViewDto getToday(String empId) throws Exception;

	/**
	 * 최근 출퇴근 정보 조회
	 * 
	 * @param empId 직원 ID
	 * @return 최근 출퇴근 정보 리스트
	 */
	List<AttendanceViewDto> getRecent(String empId) throws Exception;

	/**
	 * 나의 출퇴근 기록 조회
	 * 
	 * @param userEmail 사용자 이메일
	 * @param from      시작 날짜
	 * @param to        종료 날짜
	 * @return 출퇴근 기록 리스트
	 */
	List<AttendanceListDto> getMyAttendance(String userEmail, Date from, Date to) throws Exception;

	/**
	 * 관리자 출퇴근 기록 조회
	 * 
	 * @param cond 검색 조건
	 * @return 출퇴근 기록 페이지
	 */
	Page<AttendanceListDto> list(AdminAttendanceSearch cond) throws Exception;

	/**
	 * 부서별 월간 보고서 조회
	 * 
	 * @param start 시작 날짜
	 * @param end   종료 날짜
	 * @return 부서별 월간 보고서 리스트
	 */
	List<MonthlyDeptReportDto> getMonthlyDeptReport(Date start, Date end) throws Exception;

	int getWorkedMinutesByPeriod(String email, Date from, Date to) throws Exception;

	default int getWeeklyWorkedMinutes(String email, Date weekStart, Date weekEnd) throws Exception {
		return getWorkedMinutesByPeriod(email, weekStart, weekEnd);
	}

	default int getMonthlyWorkedMinutes(String email, Date monthStart, Date monthEnd) throws Exception {
		return getWorkedMinutesByPeriod(email, monthStart, monthEnd);
	}
}
