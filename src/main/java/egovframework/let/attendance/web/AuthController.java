package egovframework.let.attendance.web;

import static egovframework.let.attendance.web.RootController.PREFIX;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login() {
		return PREFIX + "auth/login";
	}

	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public String logout() {
		return "auth/logout";
	}

}
