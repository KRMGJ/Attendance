package egovframework.let.attendance.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Comment;

import egovframework.let.attendance.dto.request.RegistEmployeeDto;
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
@Table(name = "employee")
public class Employee {

	@Id
	@Column(length = 36)
	@Comment("직원 식별자(UUID)")
	private String id;

	@Column(length = 80, nullable = false)
	@Comment("직원 이름")
	private String name;

	@Column(length = 120, unique = true, nullable = false)
	@Comment("직원 이메일(로그인 ID)")
	private String email;

	@Column(nullable = false)
	@Comment("암호화된 비밀번호")
	private String password;

	@Column(length = 60)
	@Comment("직급 또는 직책")
	private String position;

	@Builder.Default
	@Column(name = "hire_date", nullable = false)
	@Comment("입사일")
	private LocalDate hireDate = LocalDate.now();

	@Column(name = "resign_date")
	@Comment("퇴사일")
	private LocalDate resignDate;

	@Column(name = "employment_type", length = 20, nullable = false)
	@Comment("고용 형태(정규직, 계약직 등)")
	private String employmentType;

	@Builder.Default
	@Column(name = "status", length = 20, nullable = false)
	@Comment("고용 상태(재직중, 휴직중, 퇴사 등)")
	private String status = "재직중";

	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
	private LeaveBalance leaveBalance;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<LeaveRequest> leaveRequests;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}

	public Employee(RegistEmployeeDto dto) {
		this.name = dto.getName();
		this.email = dto.getEmail();
		this.position = dto.getPosition();
		this.employmentType = dto.getEmploymentType();
	}
}
