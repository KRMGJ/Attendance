package egovframework.let.attendance.common;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.let.attendance.repository.SessionLimitRepository;

@ControllerAdvice
@Component
public class GlobalModelAdvisor {

	@Autowired
	private SessionLimitRepository sessionLimitRepository;

	@ModelAttribute("sessionExpiresAt")
	public Long sessionExpiresAt(HttpSession session) {
		return sessionLimitRepository.findBySessionId(session.getId()).map(limit -> limit.getExpiresAt().getTime())
				.orElse(null);
	}
}
