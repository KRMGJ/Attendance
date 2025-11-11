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

	public EmployeeViewDto selectViewById(String id) {
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.selectViewById", id);
	}

	public void updateProfile(EditEmployeeDto dto) {
		update("egovframework.let.attendance.repository.mybatis.EmployeeDAO.updateProfile", dto);
	}

	public int existsByEmailExcludingId(String email, String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("id", id);
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.existsByEmailExcludingId",
				params);
	}

	public int canEdit(String id, String actorUsername) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("actorUsername", actorUsername);
		return selectOne("egovframework.let.attendance.repository.mybatis.EmployeeDAO.canEdit", params);
	}

	public void updatePassword(String id, String encoded, Date updatedAt) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("encoded", encoded);
		params.put("updatedAt", updatedAt);
		update("egovframework.let.attendance.repository.mybatis.EmployeeDAO.updatePassword", params);
	}

}
