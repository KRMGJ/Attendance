package egovframework.let.attendance.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceVO {
	private String empId;
	private String workDate;
	private String checkIn;
	private String checkOut;
	private String status;
}
