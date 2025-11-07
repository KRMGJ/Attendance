package egovframework.let.attendance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class AuthController {

	/**
	 * 로그인 페이지
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login() {
		return "auth/login";
	}

	/**
	 * 로그아웃 페이지
	 */
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String logout() {
		return "auth/logout";
	}

}
