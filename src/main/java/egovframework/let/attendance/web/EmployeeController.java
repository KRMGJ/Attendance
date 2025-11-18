package egovframework.let.attendance.web;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Resource(name = "employeeService")
	private EmployeeService employeeService;

	/**
	 * 사원 상세 조회
	 */
	@PreAuthorize("hasRole('ROLE_HR') or #username == authentication.name")
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	public String employeeDetail(@RequestParam String id, Model model) throws Exception {
		EmployeeViewDto employee = employeeService.getEmployeeDetail(id);
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/**
	 * 사원 정보 수정 폼
	 */
	@PreAuthorize("hasRole('ROLE_HR')")
	@RequestMapping(value = "/edit.do", method = RequestMethod.GET)
	public String editForm(@RequestParam("id") String id, Model model) throws Exception {
		EmployeeViewDto v = employeeService.loadView(id);
		model.addAttribute("employee", v);
		return "employee/edit";
	}

	/**
	 * 사원 정보 수정 처리
	 */
	@PreAuthorize("hasRole('ROLE_HR')")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	public String editSubmit(EditEmployeeDto dto, Model model, Principal principal) throws Exception {
		employeeService.edit(dto, principal.getName());
		return "redirect:/employee/detail.do?id=" + dto.getId();
	}

	/**
	 * 사원 퇴사 처리
	 */
	@PreAuthorize("hasRole('ROLE_HR')")
	@RequestMapping(value = "/resign.do", method = RequestMethod.POST)
	public String retireEmployee(@RequestParam("id") String id) throws Exception {
		employeeService.resign(id);
		return "redirect:/employee/detail.do?id=" + id;
	}

	/**
	 * 사원 복직 처리
	 */
	@PreAuthorize("hasRole('ROLE_HR')")
	@RequestMapping(value = "/reactivate.do", method = RequestMethod.POST)
	public String rejoinEmployee(@RequestParam("id") String id) throws Exception {
		employeeService.reactivate(id);
		return "redirect:/employee/detail.do?id=" + id;
	}

}
