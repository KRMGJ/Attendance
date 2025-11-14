package egovframework.let.attendance.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.entity.SessionLimit;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.repository.SessionLimitRepository;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Resource(name = "employeeRepository")
	private EmployeeRepository employeeRepository;

	@Resource(name = "sessionLimitRepository")
	private SessionLimitRepository sessionLimitRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		String username = authentication.getName();
		Employee emp = employeeRepository.findByEmail(username).orElse(null);

		HttpSession session = request.getSession(true);

		if (emp != null) {
			session.setAttribute("empName", emp.getName());
		}

		SessionLimit limit = SessionLimit.create(session.getId(), username, 60);
		sessionLimitRepository.save(limit);

		response.sendRedirect(request.getContextPath() + "/main.do");
	}
}