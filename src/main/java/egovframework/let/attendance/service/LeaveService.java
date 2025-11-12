package egovframework.let.attendance.service;

import java.util.Date;

import org.springframework.data.domain.Page;

import egovframework.let.attendance.dto.request.NewLeaveDto;
import egovframework.let.attendance.dto.response.LeaveRequestListDto;
import egovframework.let.attendance.entity.LeaveBalance;

public interface LeaveService {
	/**
	 * 남은 휴가 일수 조회
	 * 
	 * @param empId 직원 ID
	 * @return 남은 휴가 일수 정보
	 */
	LeaveBalance getRemaining(String empId) throws Exception;

	/**
	 * 휴가 신청
	 * 
	 * @param userEmail 사용자 이메일
	 * @param dto       휴가 신청 정보
	 * @return 신청 결과 메시지
	 */
	String request(String userEmail, NewLeaveDto dto) throws Exception;

	/**
	 * 나의 휴가 신청 내역 조회
	 * 
	 * @param userEmail 사용자 이메일
	 * @return 휴가 신청 내역 리스트
	 */
	Page<LeaveRequestListDto> myRequests(String userEmail, int page, int size) throws Exception;

	/**
	 * 휴가 신청 취소
	 * 
	 * @param id    휴가 신청 ID
	 * @param empId 직원 ID
	 */
	void cancel(String id, String empId) throws Exception;

	/**
	 * 휴가 신청 승인
	 * 
	 * @param id               휴가 신청 ID
	 * @param approverUsername 승인자 사용자 이름
	 */
	void approve(String id, String approverUsername) throws Exception;

	/**
	 * 휴가 신청 거절
	 * 
	 * @param id               휴가 신청 ID
	 * @param approverUsername 승인자 사용자 이름
	 */
	void reject(String id, String approverUsername) throws Exception;

	/**
	 * 월별 휴가 일수 자동 부여
	 * 
	 * @param targetDate 대상 날짜
	 */
	void grantMonthlyAccrualIfEligible(Date targetDate) throws Exception;

	/**
	 * 지난달 월별 휴가 일수 부여가 완료되었는지 확인하고, 완료되지 않았다면 부여 작업을 수행
	 */
	void ensureLastMonthMonthlyAccrualClosed() throws Exception;

	/**
	 * 입사 기념일 기준 연차 휴가 일수 부여
	 * 
	 * @param today 오늘 날짜
	 */
	void grantAnnualByAnniversary(Date today) throws Exception;

	/**
	 * 달력 연도 기준 연차 휴가 일수 부여
	 * 
	 * @param year 대상 연도
	 */
	void grantAnnualByCalendarYear(int year) throws Exception;
}
