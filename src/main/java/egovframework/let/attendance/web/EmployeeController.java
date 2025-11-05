package egovframework.let.attendance.web;

import static egovframework.let.attendance.web.RootController.PREFIX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.let.attendance.dto.RegistEmployeeDto;
import egovframework.let.attendance.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value = "/join.do", method = RequestMethod.GET)
	public String joinForm(Model model) {
		model.addAttribute("registEmployeeDto", new RegistEmployeeDto());
		return PREFIX + "employee/join";
	}

	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
	public String join(@ModelAttribute("registEmployeeDto") RegistEmployeeDto dto) throws Exception {
		employeeService.register(dto);
		return "redirect:/auth/login.do";
	}
}
