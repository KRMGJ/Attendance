package egovframework.let.attendance.service;

import egovframework.let.attendance.dto.RegistEmployeeDto;

public interface EmployeeService {
	void register(RegistEmployeeDto dto) throws Exception;
}
