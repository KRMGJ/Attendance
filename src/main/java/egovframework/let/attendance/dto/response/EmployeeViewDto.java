package egovframework.let.attendance.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeViewDto {
	private String id;
	private String name;
	private String email;
	private String position;
	private String employmentType;
	private String hireDate;
	private String resignDate;
	private String updatedAt;
}
