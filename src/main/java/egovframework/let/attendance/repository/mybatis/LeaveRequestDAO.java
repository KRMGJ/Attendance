package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveRequest;

@Repository("leaveRequestDAO")
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
		return selectOne("leaveRequestDAO.countOverlap", p);
	}

	/**
	 * 특정 직원의 휴가 신청 건수 조회
	 * 
	 * @param empId 직원 ID
	 * @return 휴가 신청 건수
	 */
	public long countMine(String empId) {
		return selectOne("leaveRequestDAO.countMine", empId);
	}

	/**
	 * 특정 직원의 휴가 신청 내역 페이지 조회
	 * 
	 * @param empId  직원 ID
	 * @param offset 시작 오프셋
	 * @param limit  조회 건수
	 * @return 휴가 신청 내역 리스트
	 */
	public List<LeaveRequest> selectMinePage(String empId, int offset, int limit) {
		Map<String, Object> p = new HashMap<>();
		p.put("empId", empId);
		p.put("offset", offset);
		p.put("limit", limit);
		return selectList("leaveRequestDAO.selectMinePage", p);
	}

	/**
	 * 특정 상태의 휴가 신청 건수 조회
	 * 
	 * @param string 상태 문자열
	 * @return 휴가 신청 건수
	 */
	public long countByStatus(String string) {
		return selectOne("leaveRequestDAO.countByStatus", string);
	}

	/**
	 * 특정 상태의 휴가 신청 내역 페이지 조회
	 * 
	 * @param string 상태 문자열
	 * @param offset 시작 오프셋
	 * @param limit  조회 건수
	 * @return 휴가 신청 내역 리스트
	 */
	public List<LeaveRequest> selectByStatusPage(String string, int offset, int limit) {
		Map<String, Object> p = new HashMap<>();
		p.put("status", string);
		p.put("offset", offset);
		p.put("limit", limit);
		return selectList("leaveRequestDAO.selectByStatusPage", p);
	}

}
