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

import org.hibernate.annotations.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attendance")
public class Attendance {

	@Id
	@Column(length = 36)
	@Comment("근태 기록 식별자(UUID)")
	private String id;

	@Column(name = "emp_id", nullable = false, length = 36)
	@Comment("직원 식별자(EMPLOYEE.ID 참조)")
	private String empId;

	@Column(name = "work_date", nullable = false)
	@Comment("근무 날짜")
	private LocalDate workDate;

	@Builder.Default
	@Column(name = "check_in")
	@Comment("출근 시간")
	private LocalDateTime checkIn = LocalDateTime.now();

	@Column(name = "check_out")
	@Comment("퇴근 시간")
	private LocalDateTime checkOut;

	@Column(length = 20, nullable = false)
	@Comment("근무 상태(정상, 지각, 조퇴, 결근 등)")
	private String status;

	@Builder.Default
	@Column(name = "overtime_minutes", nullable = false)
	@Comment("초과 근무 시간(분 단위)")
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
