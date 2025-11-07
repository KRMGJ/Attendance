package egovframework.let.attendance.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistEmployeeDto {
	private String name;
	private String email;
	private String password;
	private String position;
	private String employmentType;
}
