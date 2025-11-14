package egovframework.let.attendance.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
public class AdminGrantLogSearch {
	private String q;
	private Integer year;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date from;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date to;
	private String reason;
	private String kind;
	private int page;
	private int size;
}
