package egovframework.let.attendance.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.let.attendance.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@RequestMapping(value = "/checkin.do", method = RequestMethod.POST)
	public String checkIn(HttpServletRequest request, Principal principal) throws Exception {
		attendanceService.checkIn(principal.getName());
		return "redirect:/main.do";
	}

	@RequestMapping(value = "/checkout.do", method = RequestMethod.POST)
	public String checkOut(HttpServletRequest request, Principal principal) throws Exception {
		attendanceService.checkOut(principal.getName());
		return "redirect:/main.do";
	}
}
