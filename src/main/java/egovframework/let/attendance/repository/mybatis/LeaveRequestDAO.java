package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveRequestDAO extends EgovAbstractMapper {

	/**
	 * 특정 직원의 휴가 신청 기간 겹침 건수 조회
	 * 
	 * @param empId 직원 ID
	 * @param start 시작 날짜
	 * @param end   종료 날짜
	 * @return 겹침 건수
	 */
	public long countOverlap(String empId, Date start, Date end) {
		Map<String, Object> p = new HashMap<>();
		p.put("empId", empId);
		p.put("start", start);
		p.put("end", end);
		return selectOne("egovframework.let.attendance.repository.mybatis.LeaveRequestDAO.countOverlap", p);
	}

}
