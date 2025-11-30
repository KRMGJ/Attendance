package egovframework.let.attendance.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository("securityAdminDAO")
public class SecurityAdminDAO extends EgovAbstractMapper {

	/**
	 * 모든 ROLE_* 목록 조회
	 * 
	 * @return ROLE_* 목록
	 */
	public List<String> selectAllRoles() {
		return selectList("securityAdminDAO.selectAllRoles");
	}

	/**
	 * 특정 사용자의 ROLE_* 목록 조회
	 * 
	 * @param username 사용자 아이디
	 * @return ROLE_* 목록
	 */
	public List<String> selectUserRoles(String username) {
		return selectList("securityAdminDAO.selectUserRoles", username);
	}

	/**
	 * 특정 사용자의 모든 ROLE_* 삭제
	 * 
	 * @param username 사용자 아이디
	 */
	public int deleteAllUserRoles(String username) {
		return delete("securityAdminDAO.deleteAllUserRoles", username);
	}

	/**
	 * 특정 사용자에게 ROLE_* 할당
	 * 
	 * @param username  사용자 아이디
	 * @param authority ROLE_*
	 */
	public int insertUserRole(String username, String authority) {
		Map<String, String> param = new HashMap<>();
		param.put("username", username);
		param.put("authority", authority);
		return insert("securityAdminDAO.insertUserRole", param);
	}

	/**
	 * ROLE_URL 목록 조회
	 * 
	 * @return ROLE_URL 목록
	 */
	public List<Map<String, Object>> selectRoleUrls() {
		return selectList("securityAdminDAO.selectRoleUrls");
	}

	/**
	 * ROLE_URL 추가
	 * 
	 * @param authority ROLE_*
	 * @param pattern   URL 패턴
	 * @param sort      정렬 순서
	 */
	public int insertRoleUrl(String authority, String pattern, int sort) {
		Map<String, Object> param = new HashMap<>();
		param.put("authority", authority);
		param.put("pattern", pattern);
		param.put("sort", sort);
		return insert("securityAdminDAO.insertRoleUrl", param);
	}

	/**
	 * ROLE_URL 삭제
	 * 
	 * @param authority ROLE_*
	 * @param pattern   URL 패턴
	 */
	public int deleteRoleUrl(String authority, String pattern) {
		Map<String, String> param = new HashMap<>();
		param.put("authority", authority);
		param.put("pattern", pattern);
		return delete("securityAdminDAO.deleteRoleUrl", param);
	}

	/**
	 * ROLE 계층구조 조회
	 * 
	 * @return ROLE 계층구조 목록
	 */
	public List<Map<String, Object>> selectRoleHierarchy() {
		return selectList("securityAdminDAO.selectRoleHierarchy");
	}

	/**
	 * ROLE_HIERARCHY 추가
	 * 
	 * @param parent 상위 ROLE
	 * @param child  하위 ROLE
	 */
	public int insertRoleHierarchy(String parent, String child) {
		Map<String, String> param = new HashMap<>();
		param.put("parent", parent);
		param.put("child", child);
		return insert("securityAdminDAO.insertRoleHierarchy", param);
	}

	/**
	 * ROLE_HIERARCHY 삭제
	 * 
	 * @param parent 상위 ROLE
	 * @param child  하위 ROLE
	 */
	public int deleteRoleHierarchy(String parent, String child) {
		Map<String, String> param = new HashMap<>();
		param.put("parent", parent);
		param.put("child", child);
		return delete("securityAdminDAO.deleteRoleHierarchy", param);
	}

	/**
	 * ROLE_HIERARCHY 트리 구조 조회
	 * 
	 * @return ROLE_HIERARCHY 트리 구조
	 */
	public List<Map<String, Object>> selectRoleHierarchyTree() {
		return selectList("securityAdminDAO.selectRoleHierarchyTree");
	}

	/**
	 * ROLE_HIERARCHY closure 목록 조회
	 * 
	 * @return ROLE_HIERARCHY closure 목록
	 */
	public List<Map<String, Object>> selectRoleClosure() {
		return selectList("securityAdminDAO.selectRoleClosure");
	}

	/**
	 * 사용자 수 조회
	 * 
	 * @param kw   검색어
	 * @param role ROLE_*
	 * @return 사용자 수
	 */
	public long countUsers(String kw, String role) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("role", role);
		return selectOne("securityAdminDAO.countUsers", param);
	}

	/**
	 * 사용자 목록 페이지 조회
	 * 
	 * @param kw     검색어
	 * @param role   ROLE_*
	 * @param offset 시작 위치
	 * @param limit  페이지 크기
	 * @return 사용자 목록
	 */
	public List<Map<String, Object>> selectUsersPage(String kw, String role, int offset, int limit) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("role", role);
		param.put("offset", offset);
		param.put("limit", limit);
		return selectList("securityAdminDAO.selectUsersPage", param);
	}
}
