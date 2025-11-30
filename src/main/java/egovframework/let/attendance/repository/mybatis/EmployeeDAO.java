package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.entity.Employee;

@Repository("employeeDAO")
public class EmployeeDAO extends EgovAbstractMapper {

	/**
	 * 직원 상세 조회
	 * 
	 * @param id 직원 ID
	 * @return 직원 상세 정보
	 */
	public EmployeeViewDto selectViewById(String id) {
		return selectOne("employeeDAO.selectViewById", id);
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
		return selectOne("employeeDAO.existsByEmailExcludingId", params);
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
		return selectOne("employeeDAO.canEdit", params);
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
		update("employeeDAO.updatePassword", params);
	}

	/**
	 * 직원 목록 카운트
	 * 
	 * @param kw       검색 키워드
	 * @param dept     부서
	 * @param position 직위
	 * @param status   고용 상태
	 * @return 직원 수
	 */
	public int countList(String kw, String dept, String position, String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("kw", kw);
		params.put("dept", dept);
		params.put("position", position);
		params.put("status", status);
		return selectOne("employeeDAO.countList", params);
	}

	/**
	 * 직원 목록 페이징 조회
	 * 
	 * @param kw       검색 키워드
	 * @param dept     부서
	 * @param position 직위
	 * @param status   고용 상태
	 * @param offset   오프셋
	 * @param limit    페이지 크기
	 * @return 직원 목록
	 */
	public List<Employee> selectListPage(String kw, String dept, String position, String status, int offset,
			int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("kw", kw);
		params.put("dept", dept);
		params.put("position", position);
		params.put("status", status);
		params.put("offset", offset);
		params.put("limit", limit);
		return selectList("employeeDAO.selectListPage", params);
	}
}
