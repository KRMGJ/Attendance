package egovframework.let.attendance.web;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/auth/session")
public class SessionController {

	@RequestMapping(value = "/remaining.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> remaining(HttpSession session, Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(401).contentType(org.springframework.http.MediaType.APPLICATION_JSON)
					.body("{\"auth\":false}");
		}

		long now = System.currentTimeMillis();
		long last = session.getLastAccessedTime();
		int max = session.getMaxInactiveInterval();
		long remainSec = Math.max(0, ((last + max * 1000L) - now) / 1000L);

		String json = String.format("{\"auth\":true,\"remainingSeconds\":%d,\"maxInactiveInterval\":%d}", remainSec,
				max);
		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(json);
	}

	@RequestMapping(value = "/extend.do", method = RequestMethod.POST)
	public ResponseEntity<Void> extend(HttpSession session) {
		// 속성 변경으로 lastAccessTime 갱신 트리거

		session.setAttribute("_touch", System.currentTimeMillis());
		return ResponseEntity.noContent().build(); // 204
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@Builder
	public static class SessionRemainDto {
		private boolean auth;
		private long remainingSeconds;
		private int maxInactiveInterval;

		public static SessionRemainDto of(boolean auth, long remainingSeconds, int maxInactiveInterval) {
			return SessionRemainDto.builder().auth(auth).remainingSeconds(remainingSeconds)
					.maxInactiveInterval(maxInactiveInterval).build();
		}
	}
}
