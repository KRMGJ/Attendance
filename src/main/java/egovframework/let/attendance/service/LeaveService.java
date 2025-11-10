package egovframework.let.attendance.service;

import java.util.List;

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
	LeaveBalance getRemaining(String empId);

	/**
	 * 휴가 신청
	 * 
	 * @param userEmail 사용자 이메일
	 * @param dto       휴가 신청 정보
	 * @return 신청 결과 메시지
	 */
	String request(String userEmail, NewLeaveDto dto);

	/**
	 * 나의 휴가 신청 내역 조회
	 * 
	 * @param userEmail 사용자 이메일
	 * @return 휴가 신청 내역 리스트
	 */
	List<LeaveRequestListDto> myRequests(String userEmail);

	/**
	 * 휴가 신청 취소
	 * 
	 * @param id    휴가 신청 ID
	 * @param empId 직원 ID
	 */
	void cancel(String id, String empId);

	/**
	 * 휴가 신청 승인
	 * 
	 * @param id               휴가 신청 ID
	 * @param approverUsername 승인자 사용자 이름
	 */
	void approve(String id, String approverUsername);

	/**
	 * 휴가 신청 거절
	 * 
	 * @param id               휴가 신청 ID
	 * @param approverUsername 승인자 사용자 이름
	 */
	void reject(String id, String approverUsername);
}
