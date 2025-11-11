package egovframework.let.attendance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.repository.mybatis.SecurityAdminDAO;
import egovframework.let.attendance.service.SecurityAdminService;

@Service
public class SecurityAdminServiceImpl implements SecurityAdminService {

	@Autowired
	private SecurityAdminDAO securityAdminDAO;

	/**
	 * 사용자 목록 조회
	 */
	@Override
	public List<Map<String, Object>> findUsers(String q) {
		return securityAdminDAO.selectUsers(q);
	}

	/**
	 * 모든 ROLE_* 목록 조회
	 */
	@Override
	public List<String> listAllRoles() {
		return securityAdminDAO.selectAllRoles();
	}

	/**
	 * 특정 사용자의 ROLE_* 목록 조회
	 */
	@Override
	public List<String> findUserRoles(String u) {
		return securityAdminDAO.selectUserRoles(u);
	}

	/**
	 * 특정 사용자에게 ROLE_* 할당
	 */
	@Override
	@Transactional
	public void assignRoles(String username, List<String> roles) {
		securityAdminDAO.deleteAllUserRoles(username);
		if (roles != null && !roles.isEmpty()) {
			for (String r : roles) {
				securityAdminDAO.insertUserRole(username, r);
			}
		}
	}

	/**
	 * ROLE_URL 목록 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleUrls() {
		return securityAdminDAO.selectRoleUrls();
	}

	/**
	 * ROLE_URL 추가
	 */
	@Override
	public void addRoleUrl(String a, String p, int s) {
		securityAdminDAO.insertRoleUrl(a, p, s);
	}

	/**
	 * ROLE_URL 삭제
	 */
	@Override
	public void deleteRoleUrl(String a, String p) {
		securityAdminDAO.deleteRoleUrl(a, p);
	}

	/**
	 * ROLE_HIERARCHY 목록 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleHierarchy() {
		return securityAdminDAO.selectRoleHierarchy();
	}

	/**
	 * ROLE_HIERARCHY 추가
	 */
	@Override
	public void addHierarchy(String parent, String child) {
		securityAdminDAO.insertRoleHierarchy(parent, child);
	}

	/**
	 * ROLE_HIERARCHY 삭제
	 */
	@Override
	public void deleteHierarchy(String parent, String child) {
		securityAdminDAO.deleteRoleHierarchy(parent, child);
	}
}
