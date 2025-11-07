package egovframework.let.attendance.dto.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewLeaveDto {
	private String type;
	private Date startDate;
	private Date endDate;
	private String reason;
}
