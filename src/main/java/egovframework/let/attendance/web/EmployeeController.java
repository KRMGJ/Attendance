package egovframework.let.attendance.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 사원 등록 폼
	 */
	@RequestMapping(value = "/join.do", method = RequestMethod.GET)
	public String joinForm(Model model) {
		model.addAttribute("registEmployeeDto", new RegistEmployeeDto());
		return "employee/join";
	}

	/**
	 * 사원 등록 처리
	 */
	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
	public String join(@ModelAttribute("registEmployeeDto") RegistEmployeeDto dto, RedirectAttributes attributes)
			throws Exception {
		try {
			employeeService.register(dto);
			attributes.addFlashAttribute("result", "success");
		} catch (Exception e) {
			attributes.addFlashAttribute("result", "fail");
		}
		return "redirect:/employee/join.do";
	}

	/**
	 * 사원 상세 조회
	 */
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	public String employeeDetail(@RequestParam String id, Model model) {
		EmployeeViewDto employee = employeeService.getEmployeeDetail(id);
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/**
	 * 사원 정보 수정 폼
	 */
	@RequestMapping(value = "/edit.do", method = RequestMethod.GET)
	public String editForm(@RequestParam("id") String id, Model model) {
		EmployeeViewDto v = employeeService.loadView(id);
		model.addAttribute("employee", v);
		return "employee/edit"; // 질문에 준 JSP
	}

	/**
	 * 사원 정보 수정 처리
	 */
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	public String editSubmit(EditEmployeeDto dto, Model model, Principal principal) {
		employeeService.edit(dto, principal.getName());
		return "redirect:/employee/detail.do?id=" + dto.getId();
	}

}
