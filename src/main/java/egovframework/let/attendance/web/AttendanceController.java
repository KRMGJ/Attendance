package egovframework.let.attendance.web;

import static egovframework.let.attendance.common.Utils.buildPi;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.UserAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Resource(name = "attendanceService")
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
	public String checkOut(HttpServletRequest request, Principal principal, RedirectAttributes attributes)
			throws Exception {
		String result = attendanceService.checkOut(principal.getName());
		attributes.addFlashAttribute("attendanceResult", result);
		return "redirect:/main.do";
	}

	/**
	 * 나의 출퇴근 기록 조회
	 */
	@RequestMapping(value = "/my.do", method = RequestMethod.GET)
	public String myAttendance(Principal principal,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to, Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String email = principal.getName();

		if (from == null || to == null) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

			cal.set(Calendar.DAY_OF_MONTH, 1);
			from = cal.getTime();

			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			to = cal.getTime();
		}

		UserAttendanceSearch cond = UserAttendanceSearch.builder().email(email).from(from).to(to)
				.page(Math.max(page - 1, 0)).size(size).build();

		Page<AttendanceListDto> list = attendanceService.getMyAttendance(cond);

		model.addAttribute("myAttendance", list.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) list.getTotalElements()));
		model.addAttribute("paramFrom", from == null ? "" : sdf.format(from));
		model.addAttribute("paramTo", to == null ? "" : sdf.format(to));
		model.addAttribute("size", size);
		model.addAttribute("from", from);
		model.addAttribute("to", to);

		return "attendance/my";
	}

}
