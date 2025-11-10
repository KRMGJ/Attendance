package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveRequestDAO extends EgovAbstractMapper {

	public long countOverlap(String empId, Date start, Date end) {
		Map<String, Object> p = new HashMap<>();
		p.put("empId", empId);
		p.put("start", start);
		p.put("end", end);
		return selectOne("egovframework.let.attendance.repository.mybatis.LeaveRequestDAO.countOverlap", p);
	}

}
