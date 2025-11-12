package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository("leaveBalanceDAO")
public class LeaveBalanceDAO extends EgovAbstractMapper {

	/**
	 * 입사 첫 해 전일 출근한 직원 목록 조회
	 * 
	 * @param year  연도
	 * @param month 월
	 * @return 직원 ID 목록
	 */
	public List<String> selectFirstYearFullAttendanceEmployees(int year, int month) {
		Map<String, Object> params = new HashMap<>();
		params.put("year", year);
		params.put("month", month);
		return selectList("leaveBalanceDAO.selectFirstYearFullAttendanceEmployees", params);
	}

	/**
	 * 특정 직원의 월별 부여 이력 건수 조회
	 * 
	 * @param empId 직원 ID
	 * @param year  연도
	 * @return 부여 이력 건수
	 */
	public int countMonthlyGranted(String empId, Integer year) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		return selectOne("leaveBalanceDAO.countMonthlyGranted", params);
	}

	/**
	 * 특정 직원의 휴가 일수 추가
	 * 
	 * @param empId 직원 ID
	 * @param year  연도
	 * @param days  추가 일수
	 * @return 업데이트 건수
	 */
	public int addQuota(String empId, Integer year, int days) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		params.put("days", days);
		return update("leaveBalanceDAO.addQuota", params);
	}

	/**
	 * 휴가 부여 이력 삽입
	 * 
	 * @param id        이력 ID
	 * @param empId     직원 ID
	 * @param year      연도
	 * @param reason    부여 사유
	 * @param days      부여 일수
	 * @param grantedAt 부여 일자
	 * @return 삽입 건수
	 */
	public int insertGrantLog(String id, String empId, Integer year, String reason, int days, Date grantedAt) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("empId", empId);
		params.put("year", year);
		params.put("reason", reason);
		params.put("days", days);
		params.put("grantedAt", grantedAt);
		return insert("leaveBalanceDAO.insertGrantLog", params);
	}

	/**
	 * 특정 직원의 연간 휴가 일수 설정
	 * 
	 * @param empId 직원 ID
	 * @param year  연도
	 * @param grant 부여 일수
	 * @return 업데이트 건수
	 */
	public int setAnnualQuota(String empId, Integer year, int grant) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		params.put("grant", grant);
		return update("leaveBalanceDAO.setAnnualQuota", params);
	}

	/**
	 * 휴가 잔여일 정보 업서트
	 * 
	 * @param id    휴가 잔여일 ID
	 * @param empId 직원 ID
	 * @param year  연도
	 * @return 업서트 건수
	 */
	public int upsertLeaveBalance(String id, String empId, Integer year) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("empId", empId);
		params.put("year", year);
		return insert("leaveBalanceDAO.upsertLeavePolicy", params);
	}

	/**
	 * 오늘이 입사기념일인 직원 목록 조회
	 * 
	 * @param today 오늘 날짜
	 * @return 직원 목록
	 */
	public List<Map<String, Object>> selectEmployeesWithAnniversaryToday(Date today) {
		return selectList("leaveBalanceDAO.selectEmployeesWithAnniversaryToday", today);
	}

	/**
	 * 특정 연도에 활성 상태인 직원 목록 조회
	 * 
	 * @param year 연도
	 * @return 직원 목록
	 */
	public List<Map<String, Object>> selectActiveEmployeesOnYear(int year) {
		return selectList("leaveBalanceDAO.selectActiveEmployeesOnYear", year);
	}

	/**
	 * 월별 부여 중복 데이터 정리
	 * 
	 * @param now 현재 날짜
	 * @return 정리된 건수
	 */
	public int fixMonthlyAccrualDuplicate(Date now) {
		return update("leaveBalanceDAO.fixMonthlyAccrualDuplicate", now);
	}

}
