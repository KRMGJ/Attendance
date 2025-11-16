package egovframework.let.attendance.web;

import static egovframework.let.attendance.common.Utils.buildPi;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.dto.request.AdminGrantLogSearch;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.dto.response.MonthlyDeptReportDto;
import egovframework.let.attendance.entity.LeaveRequest;
import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.EmployeeService;
import egovframework.let.attendance.service.LeaveService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Resource(name = "attendanceService")
	private AttendanceService attendanceService;

	@Resource(name = "employeeService")
	private EmployeeService employeeService;

	@Resource(name = "leaveService")
	private LeaveService leaveService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		fmt.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(fmt, true));
	}

	/**
	 * 사원 등록 폼
	 */
	@RequestMapping(value = "/employee/join.do", method = RequestMethod.GET)
	public String joinForm(Model model) {
		model.addAttribute("registEmployeeDto", new RegistEmployeeDto());
		return "employee/join";
	}

	/**
	 * 사원 등록 처리
	 */
	@RequestMapping(value = "/employee/join.do", method = RequestMethod.POST)
	public String join(@ModelAttribute("registEmployeeDto") RegistEmployeeDto dto, RedirectAttributes attributes)
			throws Exception {
		try {
			employeeService.register(dto);
			attributes.addFlashAttribute("result", "success");
		} catch (Exception e) {
			attributes.addFlashAttribute("result", "fail");
		}
		return "redirect:/employee/join.do";
	}

	/**
	 * 출퇴근 기록 조회
	 */
	@RequestMapping(value = "/attendance/list.do", method = RequestMethod.GET)
	public String list(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "from", required = false) Date from,
			@RequestParam(value = "to", required = false) Date to,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Model model) throws Exception {

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
	public String list(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "dept", required = false) String dept,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Model model) throws Exception {

		AdminEmployeeSearch cond = AdminEmployeeSearch.builder().q(q).dept(dept).status(status)
				.page(Math.max(page - 1, 0)).size(size).build();

		Page<EmployeeViewDto> list = employeeService.list(cond);

		model.addAttribute("list", list.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) list.getTotalElements()));
		model.addAttribute("paramQ", q);
		model.addAttribute("paramDept", dept);
		model.addAttribute("paramStatus", status);
		model.addAttribute("size", size);
		return "employee/list";
	}

	/**
	 * 휴가 신청 승인 대기 목록 조회
	 */
	@RequestMapping(value = "/leave/pending.do", method = RequestMethod.GET)
	public String pending(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size,
			Model model) throws Exception {

		Page<LeaveRequest> pr = leaveService.pending(page, size);
		model.addAttribute("pending", pr.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) pr.getTotalElements()));
		model.addAttribute("size", size);
		return "leave/admin_approve";
	}

	/**
	 * 휴가 신청 승인
	 */
	@RequestMapping(value = "/leave/approve.do", method = RequestMethod.POST)
	public String approve(@RequestParam String id, Principal principal) throws Exception {
		leaveService.approve(id, principal.getName());
		return "redirect:/admin/leave/pending.do?ok=approved";
	}

	/**
	 * 휴가 신청 거절
	 */
	@RequestMapping(value = "/leave/reject.do", method = RequestMethod.POST)
	public String reject(@RequestParam String id, Principal principal) throws Exception {
		leaveService.reject(id, principal.getName());
		return "redirect:/admin/leave/pending.do?ok=rejected";
	}

	/**
	 * 부서별 월간 보고서
	 */
	@RequestMapping(value = "/report/monthly.do", method = RequestMethod.GET)
	public String monthly(@RequestParam(value = "ym", required = false) String ym, Model model) throws Exception {
		Date start = firstDayOfMonth(ym);
		Date end = lastMomentOfMonth(start);
		List<MonthlyDeptReportDto> report = attendanceService.getMonthlyDeptReport(start, end);
		model.addAttribute("report", report);
		model.addAttribute("ym", ymValue(start));
		return "report/monthly";
	}

	/**
	 * 부서별 월간 보고서 CSV 다운로드
	 */
	@RequestMapping(value = "/report/monthlyCsv.do", method = RequestMethod.GET)
	public void downloadMonthlyCsv(@RequestParam String ym, HttpServletResponse response) throws Exception {

		Date start = firstDayOfMonth(ym);
		Date end = lastMomentOfMonth(start);

		List<MonthlyDeptReportDto> report = attendanceService.getMonthlyDeptReport(start, end);

		String filename = URLEncoder.encode("월별부서리포트_" + ym + ".csv", "UTF-8").replaceAll("\\+", "%20");

		response.setContentType("text/csv; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		PrintWriter writer = response.getWriter();
		writer.write('\uFEFF');

		writer.println("부서,인원,근무일수,총근무시간(분),총연장(분),야간연장(분),주말연장(분),지각,조퇴,결근,휴가,병가,1인평균근무(분),1인평균연장(분)");

		for (MonthlyDeptReportDto r : report) {
			writer.print(csv(r.getDepartment()));
			writer.print(',');
			writer.print(r.getHeadcount());
			writer.print(',');
			writer.print(r.getWorkDays());
			writer.print(',');
			writer.print(r.getTotalMinutes());
			writer.print(',');
			writer.print(r.getTotalOvertime());
			writer.print(',');
			writer.print(r.getNightOvertime());
			writer.print(',');
			writer.print(r.getWeekendOvertime());
			writer.print(',');
			writer.print(r.getLateCount());
			writer.print(',');
			writer.print(r.getEarlyLeaveCount());
			writer.print(',');
			writer.print(r.getAbsentCount());
			writer.print(',');
			writer.print(r.getLeaveDays());
			writer.print(',');
			writer.print(r.getSickLeaveDays());
			writer.print(',');
			writer.print(r.getAvgMinutes());
			writer.print(',');
			writer.print(r.getAvgOvertimePerCapita());
			writer.println();
		}
		writer.flush();
	}

	/**
	 * 휴가 일수 부여 내역 조회
	 */
	@RequestMapping(value = "/leave/grants.do", method = RequestMethod.GET)
	public String list(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "reason", required = false) String reason,
			@RequestParam(value = "kind", required = false) String kind,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, Model model) throws Exception {

		AdminGrantLogSearch cond = AdminGrantLogSearch.builder().q(q).year(year).reason(reason).kind(kind).from(from)
				.to(to).page(Math.max(page - 1, 0)).size(size).build();

		Page<Map<String, Object>> list = leaveService.listLeaveGrants(cond);
		model.addAttribute("list", list.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) list.getTotalElements()));
		model.addAttribute("paramQ", q);
		model.addAttribute("paramFrom", from);
		model.addAttribute("paramTo", to);
		model.addAttribute("paramYear", year);
		model.addAttribute("paramReason", reason);
		model.addAttribute("paramKind", kind);
		model.addAttribute("size", size);

		return "leave/admin_grants";
	}

	/**
	 * 월의 첫날 구하기
	 * 
	 * @param ym "yyyy-MM" 형식의 문자열
	 * @return 월의 첫날 Date 객체
	 * @throws ParseException
	 */
	private Date firstDayOfMonth(String ym) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		if (ym == null || ym.isEmpty()) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.parse(ym + "-01");
	}

	/**
	 * 월의 마지막 순간 구하기
	 * 
	 * @param start 월의 첫날 Date 객체
	 * @return 월의 마지막 순간 Date 객체
	 */
	private Date lastMomentOfMonth(Date start) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}

	/**
	 * "yyyy-MM" 형식의 문자열 구하기
	 * 
	 * @param start 월의 첫날 Date 객체
	 * @return "yyyy-MM" 형식의 문자열
	 */
	private String ymValue(Date start) {
		return new SimpleDateFormat("yyyy-MM").format(start);
	}

	/**
	 * 날짜의 23:59:59.999 구하기
	 * 
	 * @param d 날짜
	 * @return 날짜의 23:59:59.999
	 */
	private Date endOfDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	private String csv(String value) {
		if (value == null) {
			return "";
		}
		String v = value;
		if (v.contains("\"")) {
			v = v.replace("\"", "\"\"");
		}
		if (v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r")) {
			return "\"" + v + "\"";
		}
		return v;
	}
}
