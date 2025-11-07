package egovframework.let.attendance.service;

import java.util.List;

public interface AttendanceService {
	List<AttendanceVO> selectAttendanceList(AttendanceVO vo);

	String checkIn(String username);

	String checkOut(String username);
}
