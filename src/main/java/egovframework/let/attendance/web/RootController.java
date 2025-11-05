package egovframework.let.attendance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

	public static final String PREFIX = "attendance/";

	@RequestMapping
	public String home() {
		return PREFIX + "home";
	}
}
