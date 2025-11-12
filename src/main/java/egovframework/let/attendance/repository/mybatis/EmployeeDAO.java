package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;

@Repository
public class EmployeeDAO extends EgovAbstractMapper {

	/**
	 * 직원 상세 조회
	 * 
	 * @param id 직원 ID
	 * @return 직원 상세 정보
	 */
	public EmployeeViewDto selectViewById(String id) {
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.selectViewById", id);
	}

	/**
	 * 직원 프로필 정보 수정
	 * 
	 * @param dto 수정할 직원 정보 DTO
	 */
	public void updateProfile(EditEmployeeDto dto) {
		update("egovframework.let.attendance.repository.mybatis.EmployeeDAO.updateProfile", dto);
	}

	/**
	 * 이메일 중복 체크 (ID 제외)
	 * 
	 * @param email 이메일
	 * @param id    제외할 직원 ID
	 * @return 중복된 이메일 건수
	 */
	public int existsByEmailExcludingId(String email, String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("id", id);
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.existsByEmailExcludingId",
				params);
	}

	/**
	 * 직원 정보 수정 권한 체크
	 * 
	 * @param id            직원 ID
	 * @param actorUsername 수정하는 사용자의 사용자 이름
	 * @return 수정 권한 여부 (1: 권한 있음, 0: 권한 없음)
	 */
	public int canEdit(String id, String actorUsername) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("actorUsername", actorUsername);
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.canEdit", params);
	}

	/**
	 * 직원 비밀번호 업데이트
	 * 
	 * @param id        직원 ID
	 * @param encoded   인코딩된 비밀번호
	 * @param updatedAt 업데이트 시각
	 */
	public void updatePassword(String id, String encoded, Date updatedAt) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("encoded", encoded);
		params.put("updatedAt", updatedAt);
		update("egovframework.let.attendance.repository.mybatis.EmployeeDAO.updatePassword", params);
	}

}
