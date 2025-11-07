package egovframework.let.attendance.dto.request;

import java.time.LocalDate;

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
	private LocalDate from;
	private LocalDate to;
	@Builder.Default
	private int page = 0;
	@Builder.Default
	private int size = 20;
}
