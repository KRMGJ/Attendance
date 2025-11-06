package egovframework.let.attendance.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String home() {
		return "main/dashboard";
	}

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
