package egovframework.let.attendance.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.NewLeaveDto;
import egovframework.let.attendance.service.LeaveService;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	@RequestMapping(value = "/new.do", method = RequestMethod.GET)
	public String showLeaveRequestForm() {
		return "leave/request_new";
	}

	@RequestMapping(value = "/new.do", method = RequestMethod.POST)
	public String submitLeaveRequest(Principal principal, @ModelAttribute NewLeaveDto dto,
			RedirectAttributes attributes) {
		String result = leaveService.request(principal.getName(), dto);
		attributes.addFlashAttribute("message", result);
		return "redirect:/leave/new.do";
	}
}
