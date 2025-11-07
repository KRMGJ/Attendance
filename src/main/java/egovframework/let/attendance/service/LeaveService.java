package egovframework.let.attendance.service;

import egovframework.let.attendance.entity.LeaveBalance;

public interface LeaveService {
	/**
	 * 남은 휴가 일수 조회
	 * 
	 * @param empId 직원 ID
	 * @return 남은 휴가 일수 정보
	 */
	LeaveBalance getRemaining(String empId);
}
