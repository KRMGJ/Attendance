package egovframework.let.attendance.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;

import egovframework.let.attendance.repository.mybatis.SecurityAdminDAO;

@Service
public class RoleHierarchyRefresher {

	@Autowired
	SecurityAdminDAO dao;

	@Autowired
	RoleHierarchy roleHierarchy;

	public void refresh() {
		List<Map<String, Object>> edges = dao.selectRoleHierarchy();

		String text = edges.stream().map(e -> e.get("PARENT_ROLE") + " > " + e.get("CHILD_ROLE"))
				.collect(Collectors.joining("\n"));

		((RoleHierarchyImpl) roleHierarchy).setHierarchy(text);
	}
}
