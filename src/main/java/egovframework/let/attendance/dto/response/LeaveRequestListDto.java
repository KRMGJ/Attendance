package egovframework.let.attendance.dto.response;

import java.util.Date;

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
public class LeaveRequestListDto {
	private String id;
	private String empId;
	private String type;
	private String startDate;
	private String endDate;
	private int days;
	private String reason;
	private String status;
	private Date createdAt;
	private String approver;
	private String approvedAt;
}
