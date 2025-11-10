package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.Attendance;

@Repository
public class AttendanceDAO extends EgovAbstractMapper {

	public List<Attendance> selectAdminPage(String kw, Date from, Date to, int offset, int limit) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("from", from);
		param.put("to", to);
		param.put("offset", offset);
		param.put("limit", limit);
		return selectList("egovframework.let.attendance.repository.mybatis.AttendanceDAO.selectAdminPage", param);
	}

	public long countAdmin(String kw, Date from, Date to) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("from", from);
		param.put("to", to);
		return selectOne("egovframework.let.attendance.repository.mybatis.AttendanceDAO.countAdmin", param);
	}

	public List<Attendance> findMyRange(String empId, Date from, Date to) {
		Map<String, Object> param = new HashMap<>();
		param.put("empId", empId);
		param.put("from", from);
		param.put("to", to);
		return selectList("egovframework.let.attendance.repository.mybatis.AttendanceDAO.findMyRange", param);
	}

}
