package egovframework.let.attendance.web;

import static egovframework.let.attendance.common.Utils.buildPi;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.let.attendance.dto.request.NewLeaveDto;
import egovframework.let.attendance.dto.response.LeaveRequestListDto;
import egovframework.let.attendance.service.LeaveService;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Resource(name = "leaveService")
	private LeaveService leaveService;

	/** 휴가 신청 폼 조회 */
	@RequestMapping(value = "/new.do", method = RequestMethod.GET)
	public String showLeaveRequestForm() {
		return "leave/request_new";
	}

	/** 휴가 신청 처리 */
	@RequestMapping(value = "/new.do", method = RequestMethod.POST)
	public String submitLeaveRequest(Principal principal, @ModelAttribute NewLeaveDto dto,
			RedirectAttributes attributes) throws Exception {
		String result = leaveService.request(principal.getName(), dto);
		attributes.addFlashAttribute("message", result);
		return "redirect:/leave/new.do";
	}

	/** 나의 휴가 신청 내역 조회 */
	@RequestMapping(value = "/requests.do", method = RequestMethod.GET)
	public String list(Principal principal, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size, Model model) throws Exception {

		Page<LeaveRequestListDto> list = leaveService.myRequests(principal.getName(), page, size);

		model.addAttribute("requests", list.getContent());
		model.addAttribute("paginationInfo", buildPi(page, size, (int) list.getTotalElements()));
		model.addAttribute("size", size);
		return "leave/request_list";
	}
}
