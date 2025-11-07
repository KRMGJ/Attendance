package egovframework.let.attendance.service;

import egovframework.let.attendance.dto.request.NewLeaveDto;
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
}
