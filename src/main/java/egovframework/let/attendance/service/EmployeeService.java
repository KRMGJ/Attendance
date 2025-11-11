package egovframework.let.attendance.service;

import java.util.List;

import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
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

	/**
	 * 직원 뷰 로드
	 * 
	 * @param id 직원 ID
	 * @return 직원 뷰 DTO
	 */
	EmployeeViewDto loadView(String id);

	/**
	 * 직원 정보 수정
	 * 
	 * @param cmd           수정할 직원 정보 DTO
	 * @param actorUsername 수정하는 사용자의 사용자 이름
	 */
	void edit(EditEmployeeDto cmd, String actorUsername);
}
