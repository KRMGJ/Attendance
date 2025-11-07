package egovframework.let.attendance.web;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.response.AttendanceListDto;
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

	@RequestMapping(value = "/my.do", method = RequestMethod.GET)
	public String myAttendance(Principal principal, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to, Model model) {

		String email = principal.getName();

		List<AttendanceListDto> rows = attendanceService.getMyAttendance(email, from, to);
		model.addAttribute("myAttendance", rows);

		model.addAttribute("from", from);
		model.addAttribute("to", to);

		return "attendance/my";
	}
}
