package egovframework.let.attendance.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.service.AttendanceVO;

@Repository
public class AttendanceDAO extends EgovAbstractMapper {

	public List<AttendanceVO> selectAttendanceList(AttendanceVO vo) {
		return selectList("AttendanceDAO.selectAttendanceList", vo);
	}
}
