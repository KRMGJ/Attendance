package egovframework.let.attendance.service;

import egovframework.let.attendance.dto.request.RegistEmployeeDto;

public interface EmployeeService {
	/**
	 * 직원 등록
	 * 
	 * @param dto 직원 등록 정보
	 */
	void register(RegistEmployeeDto dto);
}
