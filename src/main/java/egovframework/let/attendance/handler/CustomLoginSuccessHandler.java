package egovframework.let.attendance.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		String username = authentication.getName();
		Employee emp = employeeRepository.findByEmail(username).orElse(null);

		if (emp != null) {
			request.getSession().setAttribute("empName", emp.getName());
		}
		response.sendRedirect(request.getContextPath() + "/main.do");
	}
}