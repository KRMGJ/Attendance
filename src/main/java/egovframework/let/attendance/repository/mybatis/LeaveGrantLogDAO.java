package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository("leaveGrantLogDAO")
public class LeaveGrantLogDAO extends EgovAbstractMapper {

	/**
	 * 휴가 부여 로그 수 조회
	 * 
	 * @param kw     검색어
	 * @param reason 사유
	 * @param year   연도
	 * @param from   기간
	 * @param to     기간
	 * @return 로그 수
	 */
	public long countLog(String kw, String reason, String kind, Integer year, Date from, Date to) {
		Map<String, Object> params = new HashMap<>();
		params.put("kw", kw);
		params.put("reason", reason);
		params.put("kind", kind);
		params.put("year", year);
		params.put("from", from);
		params.put("to", to);
		return selectOne("leaveGrantLogDAO.countLog", kw);
	}

	/**
	 * 휴가 부여 로그 목록 조회 (페이징)
	 * 
	 * @param kw     검색어
	 * @param reason 사유
	 * @param year   연도
	 * @param from   기간
	 * @param to     기간
	 * @param offset 시작 위치
	 * @param limit  페이지 크기
	 * @return 로그 목록
	 */
	public List<Map<String, Object>> selectLogPaged(String kw, String reason, String kind, Integer year, Date from,
			Date to, int offset, int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("kw", kw);
		params.put("reason", reason);
		params.put("kind", kind);
		params.put("year", year);
		params.put("from", from);
		params.put("to", to);
		params.put("offset", offset);
		params.put("limit", limit);
		return selectList("leaveGrantLogDAO.selectLogPaged", params);
	}
}
