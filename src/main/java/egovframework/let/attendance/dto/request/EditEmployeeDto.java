package egovframework.let.attendance.dto.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditEmployeeDto {
	private String id;
	private String name;
	private String email;
	private String employeeNumber;
	private String password;
	private String department;
	private String position;
	private String employmentType;
	private Date hireDate;
	private String phone;
	private String emergencyContactPhone;
	private String address;
	private String workStartTime = "09:00";
	private String workEndTime = "18:00";
}
