package egovframework.let.attendance.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.let.attendance.entity.SessionLimit;
import egovframework.let.attendance.repository.SessionLimitRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/auth/session")
public class SessionController {

	@Resource(name = "sessionLimitRepository")
	private SessionLimitRepository sessionLimitRepository;

	/**
	 * 세션 연장
	 */
	@RequestMapping(value = "/extend.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> extend(HttpSession session) {
		SessionLimit limit = sessionLimitRepository.findBySessionId(session.getId()).orElseGet(
				() -> SessionLimit.create(session.getId(), String.valueOf(session.getAttribute("username")), 60));
		limit.extend(60);
		sessionLimitRepository.save(limit);

		long expiresAt = limit.getExpiresAt().getTime();
		long remaining = Math.max(0, (expiresAt - System.currentTimeMillis()) / 1000);

		String json = String.format("{\"expiresAt\":%d,\"remaining\":%d}", expiresAt, remaining);
		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(json);
	}
}
