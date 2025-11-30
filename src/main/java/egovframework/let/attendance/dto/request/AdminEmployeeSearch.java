package egovframework.let.attendance.dto.request;

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
public class AdminEmployeeSearch {
	private String q;
	private String dept;
	private String position;
	private String status;
	private int page;
	private int size;
}
