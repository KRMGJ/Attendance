package egovframework.let.attendance.service;

import java.util.List;

import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.entity.Employee;

public interface EmployeeService {
	/**
	 * 직원 등록
	 * 
	 * @param dto 직원 등록 정보
	 */
	void register(RegistEmployeeDto dto);

	/**
	 * 전체 직원 조회
	 * 
	 * @return 직원 리스트
	 */
	List<Employee> getAllEmployees();

	/**
	 * 직원 상세 조회
	 * 
	 * @param id 직원 ID
	 * @return 직원 상세 정보
	 */
	Employee getEmployeeDetail(String id);
}
