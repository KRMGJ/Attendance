package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.dto.response.MonthlyDeptReportDto;
import egovframework.let.attendance.entity.Attendance;

@Repository
public class AttendanceDAO extends EgovAbstractMapper {

	/**
	 * 관리자 출퇴근 기록 페이지 조회
	 * 
	 * @param kw     검색 키워드
	 * @param from   시작 날짜
	 * @param to     종료 날짜
	 * @param offset 페이지 오프셋
	 * @param limit  페이지 크기
	 * @return 출퇴근 기록 목록
	 */
	public List<Attendance> selectAdminPage(String kw, Date from, Date to, int offset, int limit) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("from", from);
		param.put("to", to);
		param.put("offset", offset);
		param.put("limit", limit);
		return selectList("egovframework.let.attendance.repository.mybatis.AttendanceDAO.selectAdminPage", param);
	}

	/**
	 * 관리자 출퇴근 기록 건수 조회
	 * 
	 * @param kw   검색 키워드
	 * @param from 시작 날짜
	 * @param to   종료 날짜
	 * @return 출퇴근 기록 건수
	 */
	public long countAdmin(String kw, Date from, Date to) {
		Map<String, Object> param = new HashMap<>();
		param.put("kw", kw);
		param.put("from", from);
		param.put("to", to);
		return selectOne("egovframework.let.attendance.repository.mybatis.AttendanceDAO.countAdmin", param);
	}

	/**
	 * 특정 직원의 출퇴근 기록 조회 (기간 범위)
	 * 
	 * @param empId 직원 ID
	 * @param from  시작 날짜
	 * @param to    종료 날짜
	 * @return 출퇴근 기록 목록
	 */
	public List<Attendance> findMyRange(String empId, Date from, Date to) {
		Map<String, Object> param = new HashMap<>();
		param.put("empId", empId);
		param.put("from", from);
		param.put("to", to);
		return selectList("egovframework.let.attendance.repository.mybatis.AttendanceDAO.findMyRange", param);
	}

	/**
	 * 부서별 월간 출퇴근 보고서 조회
	 * 
	 * @param start 시작 날짜
	 * @param end   종료 날짜
	 * @return 부서별 월간 출퇴근 보고서 목록
	 */
	public List<MonthlyDeptReportDto> selectMonthlyDeptReport(Date start, Date end) {
		Map<String, Object> param = new HashMap<>();
		param.put("start", start);
		param.put("end", end);
		return selectList("egovframework.let.attendance.repository.mybatis.AttendanceDAO.selectMonthlyDeptReport",
				param);
	}
}
