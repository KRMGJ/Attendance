package egovframework.let.attendance.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityAdminDAO extends EgovAbstractMapper {

	/**
	 * 사용자 목록 조회
	 * 
	 * @param q 검색어
	 * @return 사용자 목록
	 */
	public List<Map<String, Object>> selectUsers(String q) {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectUsers", q);
	}

	/**
	 * 모든 ROLE_* 목록 조회
	 * 
	 * @return ROLE_* 목록
	 */
	public List<String> selectAllRoles() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectAllRoles");
	}

	/**
	 * 특정 사용자의 ROLE_* 목록 조회
	 * 
	 * @param username 사용자 아이디
	 * @return ROLE_* 목록
	 */
	public List<String> selectUserRoles(String username) {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectUserRoles", username);
	}

	public int deleteAllUserRoles(String username) {
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteAllUserRoles", username);
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
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertUserRole", param);
	}

	/**
	 * ROLE_URL 목록 조회
	 * 
	 * @return ROLE_URL 목록
	 */
	public List<Map<String, Object>> selectRoleUrls() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleUrls");
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
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertRoleUrl", param);
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
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteRoleUrl", param);
	}

	/**
	 * ROLE 계층구조 조회
	 * 
	 * @return ROLE 계층구조 목록
	 */
	public List<Map<String, Object>> selectRoleHierarchy() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleHierarchy");
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
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertRoleHierarchy", param);
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
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteRoleHierarchy", param);
	}

	/**
	 * ROLE_HIERARCHY 트리 구조 조회
	 * 
	 * @return ROLE_HIERARCHY 트리 구조
	 */
	public List<Map<String, Object>> selectRoleHierarchyTree() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleHierarchyTree");
	}

	/**
	 * ROLE_HIERARCHY closure 목록 조회
	 * 
	 * @return ROLE_HIERARCHY closure 목록
	 */
	public List<Map<String, Object>> selectRoleClosure() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleClosure");
	}
}
