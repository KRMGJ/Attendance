package egovframework.let.attendance.service;

import org.springframework.data.domain.Page;

import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;

public interface EmployeeService {
	/**
	 * 직원 등록
	 * 
	 * @param dto 직원 등록 정보
	 */
	void register(RegistEmployeeDto dto) throws Exception;

	Page<EmployeeViewDto> list(AdminEmployeeSearch cond) throws Exception;

	/**
	 * 직원 상세 조회
	 * 
	 * @param id 직원 ID
	 * @return 직원 상세 정보
	 */
	EmployeeViewDto getEmployeeDetail(String id) throws Exception;

	/**
	 * 직원 뷰 로드
	 * 
	 * @param id 직원 ID
	 * @return 직원 뷰 DTO
	 */
	EmployeeViewDto loadView(String id) throws Exception;

	/**
	 * 직원 정보 수정
	 * 
	 * @param cmd           수정할 직원 정보 DTO
	 * @param actorUsername 수정하는 사용자의 사용자 이름
	 */
	void edit(EditEmployeeDto cmd, String actorUsername) throws Exception;

	/**
	 * 직원 퇴사 처리
	 * 
	 * @param id 직원 ID
	 */
	void resign(String id) throws Exception;

	/**
	 * 직원 복직 처리
	 * 
	 * @param id 직원 ID
	 */
	void reactivate(String id) throws Exception;
}
