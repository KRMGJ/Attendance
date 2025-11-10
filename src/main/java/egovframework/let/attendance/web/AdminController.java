package egovframework.let.attendance.web;

import static egovframework.let.attendance.common.Enums.PENDING;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.entity.LeaveRequest;
import egovframework.let.attendance.repository.LeaveRequestRepository;
import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.EmployeeService;
import egovframework.let.attendance.service.LeaveService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		fmt.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(fmt, true)); // allowEmpty=true
	}

	/**
	 * 출퇴근 기록 조회
	 */
	@RequestMapping(value = "/attendance/list.do", method = RequestMethod.GET)
	public String list(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "from", required = false) Date from,
			@RequestParam(value = "to", required = false) Date to,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Model model) {

		Date toEnd = to != null ? endOfDay(to) : null;

		AdminAttendanceSearch cond = AdminAttendanceSearch.builder().q(q).from(from).to(toEnd)
				.page(Math.max(page - 1, 0)).size(size).build();

		Page<AttendanceListDto> list = attendanceService.list(cond);

		model.addAttribute("list", list.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) list.getTotalElements()));
		model.addAttribute("paramQ", q);
		model.addAttribute("paramFrom", from);
		model.addAttribute("paramTo", to);
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

	@RequestMapping(value = "/leave/pending.do", method = RequestMethod.GET)
	public String pending(Model model) {
		List<LeaveRequest> pending = leaveRequestRepository.findByStatusOrderByCreatedAtAsc(PENDING);
		model.addAttribute("pending", pending);
		return "leave/admin_approve";
	}

	@RequestMapping(value = "/leave/approve.do", method = RequestMethod.POST)
	public String approve(@RequestParam String id, Principal principal) {
		leaveService.approve(id, principal.getName());
		return "redirect:/leave/admin/pending.do?ok=approved";
	}

	@RequestMapping(value = "/leave/reject.do", method = RequestMethod.POST)
	public String reject(@RequestParam String id, Principal principal) {
		leaveService.reject(id, principal.getName());
		return "redirect:/leave/admin/pending.do?ok=rejected";
	}

	private Date endOfDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	private PaginationInfo buildPi(int page, int size, int total) {
		PaginationInfo pi = new PaginationInfo();
		pi.setCurrentPageNo(page);
		pi.setRecordCountPerPage(size);
		pi.setPageSize(10);
		pi.setTotalRecordCount(total);
		return pi;
	}

}
