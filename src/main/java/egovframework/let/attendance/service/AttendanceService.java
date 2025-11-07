package egovframework.let.attendance.service;

import java.util.List;

import egovframework.let.attendance.entity.Attendance;

public interface AttendanceService {
	List<AttendanceVO> selectAttendanceList(AttendanceVO vo);

	String checkIn(String username);

	String checkOut(String username);

	Attendance getToday(String empId);

	List<Attendance> getRecent(String empId);
}
