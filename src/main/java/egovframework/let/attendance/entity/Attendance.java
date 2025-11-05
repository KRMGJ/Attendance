package egovframework.let.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attendance")
public class Attendance {

	@Id
	@Column(length = 36)
	private String id;

	@Column(name = "emp_id", nullable = false, length = 36)
	private String empId;

	@Column(name = "work_date", nullable = false)
	private LocalDate workDate;

	@Column(name = "check_in")
	private LocalDateTime checkIn;

	@Column(name = "check_out")
	private LocalDateTime checkOut;

	@Column(length = 20, nullable = false)
	private String status;

	@Column(name = "overtime_minutes", nullable = false)
	private int overtimeMinutes = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id", insertable = false, updatable = false)
	private Employee employee;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}
}
