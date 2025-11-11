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
	private String position;
	private String employmentType;
	private String password;
	private String passwordConfirm;
	private Date updatedAt;
}
