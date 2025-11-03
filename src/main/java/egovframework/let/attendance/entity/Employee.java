package egovframework.let.attendance.entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@Column(length = 36)
	private String id;

	@Column(length = 80, nullable = false)
	private String name;

	@Column(length = 120, unique = true, nullable = false)
	private String email;

	@Column(length = 60)
	private String position;

	@Column(name = "hire_date", nullable = false)
	private LocalDate hireDate;

	@Column(name = "resign_date")
	private LocalDate resignDate;

	@Column(name = "employment_type", length = 20, nullable = false)
	private String employmentType;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}
}
