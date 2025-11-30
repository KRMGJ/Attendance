package egovframework.let.attendance.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserAttendanceSearch {
	private String email;
	private Date from;
	private Date to;
	private int page;
	private int size;
}
