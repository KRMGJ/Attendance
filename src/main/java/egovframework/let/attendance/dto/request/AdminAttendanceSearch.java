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
public class AdminAttendanceSearch {
	private String q;
	private Date from;
	private Date to;
	@Builder.Default
	private int page = 0;
	@Builder.Default
	private int size = 20;
}
