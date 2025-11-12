package egovframework.let.attendance.dto.response;

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
public class EmployeeViewDto {
	private String id;
	private String name;
	private String email;
	private String position;
	private String department;
	private String employmentType;
	private String status;
	private String hireDate;
	private String resignDate;
	private String updatedAt;
}
