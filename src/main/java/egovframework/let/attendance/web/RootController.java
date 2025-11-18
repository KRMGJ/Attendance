package egovframework.let.attendance.web;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.LeaveService;
import lombok.extern.slf4j.Slf4j;

@PreAuthorize("hasRole('ROLE_USER')")
@Slf4j
@Controller
public class RootController {

	private static final TimeZone KST = TimeZone.getTimeZone("Asia/Seoul");

	@Resource(name = "employeeRepository")
	private EmployeeRepository employeeRepository;

	@Resource(name = "attendanceService")
	private AttendanceService attendanceService;

	@Resource(name = "leaveService")
	private LeaveService leaveService;

	/**
	 * 메인 대시보드
	 */
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String home(Model model, Principal principal, Authentication a) throws Exception {
		if (principal == null) {
			return "redirect:/login.do";
		}
		String username = principal.getName();
		Employee emp = employeeRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("직원 정보를 찾을 수 없음: " + username));

		Date today = new Date();

		Date monthStart = firstDayOfMonth(today);
		Date monthEnd = lastDayOfMonth(today);

		Date weekStart = weekStart(today);
		Date weekEnd = weekEnd(today);

		// 월/주 근무시간(분 단위)
		int monthlyMinutes = attendanceService.getWorkedMinutesByPeriod(username, monthStart, monthEnd);
		int weeklyMinutes = attendanceService.getWorkedMinutesByPeriod(username, weekStart, weekEnd);

		model.addAttribute("empName", emp.getName());
		model.addAttribute("today", attendanceService.getToday(emp.getId()));
		model.addAttribute("recentAttendance", attendanceService.getRecent(emp.getId()));
		model.addAttribute("leave", leaveService.getRemaining(emp.getId()));
		model.addAttribute("monthlyMinutes", monthlyMinutes);
		model.addAttribute("weeklyMinutes", weeklyMinutes);
		return "main/dashboard";
	}

	/**
	 * 로그인 페이지
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null) {
			model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
		}
		if (logout != null) {
			model.addAttribute("msg", "로그아웃되었습니다.");
		}
		return "auth/login";
	}

	private Date firstDayOfMonth(Date base) {
		Calendar cal = Calendar.getInstance(KST);
		cal.setTime(base);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date lastDayOfMonth(Date base) {
		Calendar cal = Calendar.getInstance(KST);
		cal.setTime(base);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		cal.add(Calendar.MONTH, 1); // 다음 달 1일
		cal.add(Calendar.DATE, -1); // 이번 달 마지막 날
		return cal.getTime();
	}

	private Date weekStart(Date base) {
		Calendar cal = Calendar.getInstance(KST);
		cal.setTime(base);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date weekEnd(Date base) {
		Calendar cal = Calendar.getInstance(KST);
		cal.setTime(base);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
}
