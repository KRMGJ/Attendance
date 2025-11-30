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
	private String employeeNumber;
	private String password;
	private String department;
	private String position;
	private String employmentType;
	private String status;
	private String hireDate;
	private String phone;
	private String emergencyContactPhone;
	private String address;
	private String workStartTime;
	private String workEndTime;
	private String resignDate;
	private String updatedAt;
}
