package egovframework.let.attendance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "session_limit", indexes = { @Index(name = "ix_sessionlimit_username", columnList = "username") })
@org.hibernate.annotations.Table(appliesTo = "session_limit", comment = "세션 제한 관리 테이블")
@Builder
public class SessionLimit {

	@Id
	@Column(name = "session_id", length = 100)
	@Comment("세션ID")
	private String sessionId;

	@Column(name = "emp_id", length = 36)
	@Comment("직원 식별자(UUID)")
	private String empId;

	@Column(nullable = false, length = 100)
	@Comment("사용자명")
	private String username;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires_at", nullable = false)
	@Comment("만료일시")
	private Date expiresAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Employee employee;

	public static SessionLimit create(String sessionId, String username, long minutes) {
		return SessionLimit.builder().sessionId(sessionId).username(username)
				.expiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000)).build();
	}

	public void extend(long minutes) {
		this.expiresAt = new Date(System.currentTimeMillis() + minutes * 60 * 1000);
	}
}
