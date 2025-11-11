package egovframework.let.attendance.service;

import java.util.List;
import java.util.Map;

public interface SecurityAdminService {
	/**
	 * 사용자 목록 조회
	 * 
	 * @param q 검색어
	 * @return 사용자 목록
	 */
	List<Map<String, Object>> findUsers(String q);

	/**
	 * 모든 ROLE_* 목록 조회
	 * 
	 * @return ROLE_* 목록
	 */
	List<String> listAllRoles();

	/**
	 * 특정 사용자의 ROLE_* 목록 조회
	 * 
	 * @param username 사용자 아이디
	 * @return ROLE_* 목록
	 */
	List<String> findUserRoles(String username);

	/**
	 * 특정 사용자에게 ROLE_* 할당
	 * 
	 * @param username 사용자 아이디
	 * @param roles    ROLE_* 목록
	 */
	void assignRoles(String username, List<String> roles);

	/**
	 * ROLE_URL 목록 조회
	 * 
	 * @return ROLE_URL 목록
	 */
	List<Map<String, Object>> listRoleUrls();

	/**
	 * ROLE_URL 추가
	 * 
	 * @param authority ROLE_*
	 * @param pattern   URL 패턴
	 * @param sort      정렬 순서
	 */
	void addRoleUrl(String authority, String pattern, int sort);

	/**
	 * ROLE_URL 삭제
	 * 
	 * @param authority ROLE_*
	 * @param pattern   URL 패턴
	 */
	void deleteRoleUrl(String authority, String pattern);

	/**
	 * ROLE_HIERARCHY 목록 조회
	 * 
	 * @return ROLE_HIERARCHY 목록
	 */
	List<Map<String, Object>> listRoleHierarchy();

	List<Map<String, Object>> listRoleHierarchyTree(); // ROLE, PARENT, DEPTH, IS_ROOT

	List<Map<String, Object>> listRoleClosure(); // PARENT, CHILD

	/**
	 * ROLE_HIERARCHY 추가
	 * 
	 * @param parent 상위 ROLE_*
	 * @param child  하위 ROLE_*
	 */
	void addHierarchy(String parent, String child);

	/**
	 * ROLE_HIERARCHY 삭제
	 * 
	 * @param parent 상위 ROLE_*
	 * @param child  하위 ROLE_*
	 */
	void deleteHierarchy(String parent, String child);
}
