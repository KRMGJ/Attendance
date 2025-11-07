package egovframework.let.attendance.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "leave_balance")
@Builder
public class LeaveBalance {

	@Id
	@Column(length = 36)
	@Comment("연차 잔여일 식별자(UUID)")
	private String id;

	@Column(name = "emp_id", nullable = false, length = 36)
	@Comment("직원 식별자(UUID)")
	private String empId;

	@Column(nullable = false)
	@Comment("연도")
	private int year;

	@Column(nullable = false)
	@Comment("총 연차 일수")
	private int total;

	@Column(nullable = false)
	@Comment("사용한 연차 일수")
	private int used;

	@OneToOne
	@JoinColumn(name = "emp_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Employee employee;

	@Transient
	public int setUsed(int days) {
		return this.used + days;
	}

	@Transient
	public int getRemaining() {
		return total - used;
	}

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}

}
