package egovframework.let.attendance.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.AdminSecuritySearch;
import egovframework.let.attendance.repository.mybatis.SecurityAdminDAO;
import egovframework.let.attendance.service.SecurityAdminService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("securityAdminService")
@Transactional(readOnly = true)
public class SecurityAdminServiceImpl implements SecurityAdminService {

	@Resource(name = "securityAdminDAO")
	private SecurityAdminDAO securityAdminDAO;

	/**
	 * 사용자 목록 조회
	 */
	@Override
	public Page<Map<String, Object>> findUsers(AdminSecuritySearch cond) throws Exception {
		try {
			Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
			String kw = (cond.getQ() == null || cond.getQ().trim().isEmpty()) ? null
					: "%" + cond.getQ().trim().toLowerCase() + "%";
			String role = cond.getRole();

			int offset = (int) pageable.getOffset();
			int limit = pageable.getPageSize();

			long total = securityAdminDAO.countUsers(kw, role);
			List<Map<String, Object>> content = total == 0 ? Collections.emptyList()
					: securityAdminDAO.selectUsersPage(kw, role, offset, limit);

			return new PageImpl<>(content, pageable, total);
		} catch (Exception e) {
			log.error("Error in findUsers: {}", e);
			throw e;
		}
	}

	/**
	 * 모든 ROLE_* 목록 조회
	 */
	@Override
	public List<String> listAllRoles() throws Exception {
		try {
			return securityAdminDAO.selectAllRoles();
		} catch (Exception e) {
			log.error("Error in listAllRoles: {}", e);
			throw e;
		}
	}

	/**
	 * 특정 사용자의 ROLE_* 목록 조회
	 */
	@Override
	public List<String> findUserRoles(String u) throws Exception {
		try {
			return securityAdminDAO.selectUserRoles(u);
		} catch (Exception e) {
			log.error("Error in findUserRoles for user '{}': {}", u, e);
			throw e;
		}
	}

	/**
	 * 특정 사용자에게 ROLE_* 할당
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void assignRoles(String username, List<String> roles) throws Exception {
		try {
			securityAdminDAO.deleteAllUserRoles(username);
			if (roles != null && !roles.isEmpty()) {
				for (String r : roles) {
					securityAdminDAO.insertUserRole(username, r);
				}
			}
		} catch (Exception e) {
			log.error("Error in assignRoles for user '{}': {}", username, e);
			throw e;
		}
	}

	/**
	 * ROLE_URL 목록 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleUrls() throws Exception {
		try {
			return securityAdminDAO.selectRoleUrls();
		} catch (Exception e) {
			log.error("Error in listRoleUrls: {}", e);
			throw e;
		}
	}

	/**
	 * ROLE_URL 추가
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addRoleUrl(String a, String p, int s) throws Exception {
		try {
			securityAdminDAO.insertRoleUrl(a, p, s);
		} catch (Exception e) {
			log.error("Error in addRoleUrl with authority '{}', pattern '{}', sort '{}': {}", a, p, s, e);
			throw e;
		}
	}

	/**
	 * ROLE_URL 삭제
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRoleUrl(String a, String p) throws Exception {
		try {
			securityAdminDAO.deleteRoleUrl(a, p);
		} catch (Exception e) {
			log.error("Error in deleteRoleUrl with authority '{}', pattern '{}': {}", a, p, e);
			throw e;
		}
	}

	/**
	 * ROLE_HIERARCHY 목록 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleHierarchy() throws Exception {
		try {
			return securityAdminDAO.selectRoleHierarchy();
		} catch (Exception e) {
			log.error("Error in listRoleHierarchy: {}", e);
			throw e;
		}
	}

	/**
	 * ROLE_HIERARCHY 추가
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addHierarchy(String parent, String child) throws Exception {
		try {
			securityAdminDAO.insertRoleHierarchy(parent, child);
		} catch (Exception e) {
			log.error("Error in addHierarchy with parent '{}', child '{}': {}", parent, child, e);
			throw e;
		}
	}

	/**
	 * ROLE_HIERARCHY 삭제
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteHierarchy(String parent, String child) throws Exception {
		try {
			securityAdminDAO.deleteRoleHierarchy(parent, child);
		} catch (Exception e) {
			log.error("Error in deleteHierarchy with parent '{}', child '{}': {}", parent, child, e);
			throw e;
		}
	}

	/**
	 * ROLE_HIERARCHY 트리 구조 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleHierarchyTree() throws Exception {
		try {
			return securityAdminDAO.selectRoleHierarchyTree();
		} catch (Exception e) {
			log.error("Error in listRoleHierarchyTree: {}", e);
			throw e;
		}
	}

	/**
	 * ROLE_HIERARCHY closure 목록 조회
	 */
	@Override
	public List<Map<String, Object>> listRoleClosure() throws Exception {
		try {
			return securityAdminDAO.selectRoleClosure();
		} catch (Exception e) {
			log.error("Error in listRoleClosure: {}", e);
			throw e;
		}
	}
}
