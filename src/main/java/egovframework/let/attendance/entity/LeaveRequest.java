package egovframework.let.attendance.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
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
@Table(name = "leave_request")
@Builder
public class LeaveRequest {
	@Id
	@Column(length = 36)
	@Comment("휴가신청ID")
	private String id;

	@Column(name = "emp_id", nullable = false, length = 36)
	@Comment("사원ID")
	private String empId;

	@Column(length = 20, nullable = false)
	@Comment("휴가유형")
	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	@Comment("시작일")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	@Comment("종료일")
	private Date endDate;

	@Column(nullable = false)
	@Comment("신청 일수")
	private int days; // 신청 일수(휴일 제외는 정책에 맞춰 조정)

	@Column(length = 500)
	@Comment("사유")
	private String reason;

	@Column(length = 20, nullable = false)
	@Comment("상태")
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Builder.Default
	@Column(name = "created_at", nullable = false)
	@Comment("신청일시")
	private Date createdAt = new Date();

	@Column(name = "approver", length = 100)
	@Comment("승인자")
	private String approver;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approved_at")
	@Comment("승인일시")
	private Date approvedAt;

	@ManyToOne
	@JoinColumn(name = "emp_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Employee employee;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}
}
