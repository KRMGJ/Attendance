package egovframework.let.attendance.service;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeVO {
	private String id;
	private String name;
	private String email;
	private String position;
	private LocalDate hireDate;
	private LocalDate resignDate;
	private String employmentType;
}
