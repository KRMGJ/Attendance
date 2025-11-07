package egovframework.let.attendance.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	/**
	 * 출근 처리
	 */
	@RequestMapping(value = "/checkin.do", method = RequestMethod.POST)
	public String checkIn(HttpServletRequest request, Principal principal, RedirectAttributes attributes)
			throws Exception {
		String result = attendanceService.checkIn(principal.getName());
		attributes.addFlashAttribute("attendanceResult", result);
		return "redirect:/main.do";
	}

	/**
	 * 퇴근 처리
	 */
	@RequestMapping(value = "/checkout.do", method = RequestMethod.POST)
	public String checkOut(HttpServletRequest request, Principal principal, RedirectAttributes attributes) {
		String result = attendanceService.checkOut(principal.getName());
		attributes.addFlashAttribute("attendanceResult", result);
		return "redirect:/main.do";
	}
}
