package egovframework.let.attendance.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.let.attendance.service.SecurityAdminService;

@Controller
@RequestMapping("/admin/security")
public class SecurityAdminController {

	@Autowired
	private SecurityAdminService securityAdminService;

	/**
	 * 사용자 목록 페이지
	 */
	@RequestMapping(value = "/users.do", method = RequestMethod.GET)
	public String users(@RequestParam(value = "q", required = false) String q, Model model) throws Exception {
		model.addAttribute("users", securityAdminService.findUsers(q));
		model.addAttribute("allRoles", securityAdminService.listAllRoles());
		return "security/users";
	}

	/**
	 * 특정 사용자에게 ROLE_* 할당 처리
	 */
	@RequestMapping(value = "/users/assign.do", method = RequestMethod.POST)
	public String assign(@RequestParam String username,
			@RequestParam(required = false, name = "roles") List<String> roles) throws Exception {
		securityAdminService.assignRoles(username, roles);
		return "redirect:/admin/security/users.do?username=" + username;
	}

	/**
	 * ROLE_URL 관리 페이지
	 */
	@RequestMapping(value = "/role-url.do", method = RequestMethod.GET)
	public String roleUrl(Model model) throws Exception {
		model.addAttribute("rows", securityAdminService.listRoleUrls());
		model.addAttribute("allRoles", securityAdminService.listAllRoles());
		return "security/role_url";
	}

	/**
	 * ROLE_URL 추가 처리
	 */
	@RequestMapping(value = "/role-url/add.do", method = RequestMethod.POST)
	public String addRoleUrl(@RequestParam String authority, @RequestParam String pattern,
			@RequestParam(defaultValue = "0") int sort) throws Exception {
		securityAdminService.addRoleUrl(authority, pattern, sort);
		return "redirect:/admin/security/role-url.do";
	}

	/**
	 * ROLE_URL 삭제 처리
	 */
	@RequestMapping(value = "/role-url/delete.do", method = RequestMethod.POST)
	public String delRoleUrl(@RequestParam String authority, @RequestParam String pattern) throws Exception {
		securityAdminService.deleteRoleUrl(authority, pattern);
		return "redirect:/admin/security/role-url.do";
	}

	/**
	 * ROLE_HIERARCHY 관리 페이지
	 */
	@RequestMapping(value = "/role-hierarchy.do", method = RequestMethod.GET)
	public String roleHierarchy(Model model) throws Exception {
		model.addAttribute("tree", securityAdminService.listRoleHierarchyTree());
		model.addAttribute("closure", securityAdminService.listRoleClosure());
		model.addAttribute("edges", securityAdminService.listRoleHierarchy());
		model.addAttribute("allRoles", securityAdminService.listAllRoles());
		return "security/role_hierarchy";
	}

	/**
	 * ROLE_HIERARCHY 추가 처리
	 */
	@RequestMapping(value = "/role-hierarchy/add.do", method = RequestMethod.POST)
	public String addHierarchy(@RequestParam String parentRole, @RequestParam String childRole) throws Exception {
		securityAdminService.addHierarchy(parentRole, childRole);
		return "redirect:/admin/security/role-hierarchy.do";
	}

	/**
	 * ROLE_HIERARCHY 삭제 처리
	 */
	@RequestMapping(value = "/role-hierarchy/delete.do", method = RequestMethod.POST)
	public String delHierarchy(@RequestParam String parentRole, @RequestParam String childRole) throws Exception {
		securityAdminService.deleteHierarchy(parentRole, childRole);
		return "redirect:/admin/security/role-hierarchy.do";
	}
}