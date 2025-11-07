package egovframework.let.attendance.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.RegistEmployeeDto;
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
}
