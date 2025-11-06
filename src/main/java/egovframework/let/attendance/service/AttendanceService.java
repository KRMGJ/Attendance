package egovframework.let.attendance.service;

import java.util.List;

public interface AttendanceService {
	List<AttendanceVO> selectAttendanceList(AttendanceVO vo) throws Exception;

	void checkIn(String username) throws Exception;

	void checkOut(String username) throws Exception;
}
