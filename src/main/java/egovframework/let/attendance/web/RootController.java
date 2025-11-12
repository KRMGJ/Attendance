package egovframework.let.attendance.web;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
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

		model.addAttribute("empName", emp.getName());
		model.addAttribute("today", attendanceService.getToday(emp.getId()));
		model.addAttribute("recentAttendance", attendanceService.getRecent(emp.getId()));
		model.addAttribute("leave", leaveService.getRemaining(emp.getId()));
		log.info(a.getAuthorities().toString());
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
}
