package egovframework.let.attendance.dto.response;

import java.time.LocalDate;

import egovframework.let.attendance.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AttendanceListDto {
	private String id;
	private String empId;
	private LocalDate workDate;
	private String checkIn;
	private String checkOut;
	private String status;
	private int overtimeMinutes;
	private Employee employee;
}
