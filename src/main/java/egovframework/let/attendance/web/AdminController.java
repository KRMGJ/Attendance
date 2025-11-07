package egovframework.let.attendance.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.EmployeeService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 출퇴근 기록 조회
	 */
	@RequestMapping(value = "/attendance/list.do", method = RequestMethod.GET)
	public String list(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Model model) {

		AdminAttendanceSearch cond = AdminAttendanceSearch.builder().q(q).from(from).to(to).page(page).size(size)
				.build();

		List<AttendanceListDto> list = attendanceService.list(cond).getContent();
		model.addAttribute("list", list);
		model.addAttribute("total", attendanceService.list(cond).getTotalElements());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		return "attendance/admin_list";
	}

	/**
	 * 사원 목록 조회
	 */
	@RequestMapping(value = "/employee/list.do", method = RequestMethod.GET)
	public String employeeList(Model model) {
		List<Employee> list = employeeService.getAllEmployees();
		model.addAttribute("employees", list);
		return "employee/list";
	}

}
