package egovframework.let.attendance.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.let.attendance.service.AttendanceService;
import egovframework.let.attendance.service.AttendanceVO;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@RequestMapping("/attendance/list.do")
	public String selectAttendanceList(Model model, AttendanceVO vo) throws Exception {
		List<AttendanceVO> resultList = attendanceService.selectAttendanceList(vo);
		model.addAttribute("resultList", resultList);
		return "attendance/AttendanceList";
	}
}
