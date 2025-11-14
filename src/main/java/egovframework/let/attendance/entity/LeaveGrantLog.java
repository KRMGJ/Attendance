package egovframework.let.attendance.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

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
@Builder
@Entity
@Table(name = "leave_grant_log", uniqueConstraints = {
		@UniqueConstraint(name = "uk_grant_emp_year_reason_dt", columnNames = { "emp_id", "year", "reason",
				"granted_at" }) }, indexes = { @Index(name = "ix_grant_emp_year", columnList = "emp_id, year") })
@org.hibernate.annotations.Table(appliesTo = "leave_grant_log", comment = "연차 지급 이력 테이블")
public class LeaveGrantLog {
	@Id
	@Column(length = 36)
	@Comment("UUID")
	private String id;

	@Column(name = "emp_id", nullable = false, length = 36)
	@Comment("사원ID")
	private String empId;

	@Column(name = "year", nullable = false)
	@Comment("연도")
	private Integer year;

	@Column(name = "reason", length = 30, nullable = false)
	@Comment("지급사유")
	private String reason;

	@Column(name = "change_days", nullable = false)
	@Comment("변경일수(양수=지급, 음수=차감)")
	private Integer changeDays;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "granted_at", nullable = false)
	@Comment("지급일시")
	private Date grantedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "emp_id", referencedColumnName = "emp_id", insertable = false, updatable = false),
			@JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false) })
	private LeaveBalance leaveBalance;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}
}
