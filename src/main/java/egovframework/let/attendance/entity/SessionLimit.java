package egovframework.let.attendance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session_limit")
@org.hibernate.annotations.Table(appliesTo = "session_limit", comment = "세션 제한 관리 테이블")
@Builder
public class SessionLimit {

	@Id
	@Column(length = 100)
	@Comment("세션ID")
	private String sessionId;

	@Column(nullable = false, length = 100)
	@Comment("사용자명")
	private String username;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Comment("만료일시")
	private Date expiresAt;

	public static SessionLimit create(String sessionId, String username, long minutes) {
		return SessionLimit.builder().sessionId(sessionId).username(username)
				.expiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000)).build();
	}

	public void extend(long minutes) {
		this.expiresAt = new Date(System.currentTimeMillis() + minutes * 60 * 1000);
	}
}
