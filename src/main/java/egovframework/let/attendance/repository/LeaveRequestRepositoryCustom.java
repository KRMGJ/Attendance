package egovframework.let.attendance.repository;

import java.util.Date;

public interface LeaveRequestRepositoryCustom {
	long countOverlap(String empId, Date start, Date end);
}
