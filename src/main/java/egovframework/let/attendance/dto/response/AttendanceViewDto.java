package egovframework.let.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AttendanceViewDto {
	private String workDate;
	private String checkIn;
	private String checkOut;
	private String status;
	private int workedMinutes;
}
