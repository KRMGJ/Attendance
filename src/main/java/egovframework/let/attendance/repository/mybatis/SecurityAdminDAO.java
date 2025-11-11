package egovframework.let.attendance.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityAdminDAO extends EgovAbstractMapper {

	public List<Map<String, Object>> selectUsers(String q) {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectUsers", q);
	}

	public List<String> selectAllRoles() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectAllRoles");
	}

	public List<String> selectUserRoles(String username) {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectUserRoles", username);
	}

	public int deleteAllUserRoles(String username) {
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteAllUserRoles", username);
	}

	public int insertUserRole(String username, String authority) {
		Map<String, String> param = new HashMap<>();
		param.put("username", username);
		param.put("authority", authority);
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertUserRole", param);
	}

	public List<Map<String, Object>> selectRoleUrls() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleUrls");
	}

	public int insertRoleUrl(String authority, String pattern, int sort) {
		Map<String, Object> param = new HashMap<>();
		param.put("authority", authority);
		param.put("pattern", pattern);
		param.put("sort", sort);
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertRoleUrl", param);
	}

	public int deleteRoleUrl(String authority, String pattern) {
		Map<String, String> param = new HashMap<>();
		param.put("authority", authority);
		param.put("pattern", pattern);
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteRoleUrl", param);
	}

	public List<Map<String, Object>> selectRoleHierarchy() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleHierarchy");
	}

	public int insertRoleHierarchy(String parent, String child) {
		Map<String, String> param = new HashMap<>();
		param.put("parent", parent);
		param.put("child", child);
		return insert("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.insertRoleHierarchy", param);
	}

	public int deleteRoleHierarchy(String parent, String child) {
		Map<String, String> param = new HashMap<>();
		param.put("parent", parent);
		param.put("child", child);
		return delete("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.deleteRoleHierarchy", param);
	}

	public List<Map<String, Object>> selectRoleHierarchyTree() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleHierarchyTree");
	}

	public List<Map<String, Object>> selectRoleClosure() {
		return selectList("egovframework.let.attendance.repository.mybatis.SecurityAdminDAO.selectRoleClosure");
	}
}
