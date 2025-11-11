package egovframework.let.attendance.repository.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveBalanceDAO extends EgovAbstractMapper {

	public List<String> selectFirstYearFullAttendanceEmployees(int year, int month) {
		Map<String, Object> params = new HashMap<>();
		params.put("year", year);
		params.put("month", month);
		return selectList(
				"egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.selectFirstYearFullAttendanceEmployees",
				params);
	}

	public int countMonthlyGranted(String empId, Integer year) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		return selectOne("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.countMonthlyGranted", params);
	}

	public int addQuota(String empId, Integer year, int days) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		params.put("days", days);
		return update("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.addQuota", params);
	}

	public int insertGrantLog(String id, String empId, Integer year, String reason, int days, Date grantedAt) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("empId", empId);
		params.put("year", year);
		params.put("reason", reason);
		params.put("days", days);
		params.put("grantedAt", grantedAt);
		return insert("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.insertGrantLog", params);
	}

	public int setAnnualQuota(String empId, Integer year, int grant) {
		Map<String, Object> params = new HashMap<>();
		params.put("empId", empId);
		params.put("year", year);
		params.put("grant", grant);
		return update("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.setAnnualQuota", params);
	}

	public int upsertLeaveBalance(String id, String empId, Integer year) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("empId", empId);
		params.put("year", year);
		return insert("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.upsertLeavePolicy", params);
	}

	public List<Map<String, Object>> selectEmployeesWithAnniversaryToday(Date today) {
		return selectList(
				"egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.selectEmployeesWithAnniversaryToday",
				today);
	}

	public List<Map<String, Object>> selectActiveEmployeesOnYear(int year) {
		return selectList("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.selectActiveEmployeesOnYear",
				year);
	}

	public int fixMonthlyAccrualDuplicate(Date now) {
		return update("egovframework.let.attendance.repository.mybatis.LeaveBalanceDAO.fixMonthlyAccrualDuplicate",
				now);
	}

}
