package egovframework.let.attendance.dto.response;

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
public class MonthlyDeptReportDto {
	private String department;
	private int headcount;
	private int workDays;
	private int totalMinutes;
	private int totalOvertime;
	private int nightOvertime;
	private int weekendOvertime;
	private int lateCount;
	private int earlyLeaveCount;
	private int absentCount;
	private int leaveDays;
	private int sickLeaveDays;
	private int avgMinutes;
	private int avgOvertimePerCapita;
}