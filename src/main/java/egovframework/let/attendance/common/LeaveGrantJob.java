package egovframework.let.attendance.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.let.attendance.service.LeaveService;

@Service
public class LeaveGrantJob {

	@Autowired
	private LeaveService leaveService;

	/** 입사 1년 미만 월만근 시 1일 부여 */
	public void runDailyMonthlyAccrual() throws Exception {
		Date today = new Date();
		// 전일 기준 처리
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date targetDate = cal.getTime();
		leaveService.grantMonthlyAccrualIfEligible(targetDate);
	}

	/** 매월 1일 세이프가드(중복 부여 방지 재검증) */
	public void runMonthlySafeguard() throws Exception {
		leaveService.ensureLastMonthMonthlyAccrualClosed();
	}

	/** 입사기념일 기준 연차 갱신 */
	public void runAnniversaryAnnualGrant() throws Exception {
		Date today = new Date();
		leaveService.grantAnnualByAnniversary(today);
	}

	/** 캘린더 기준(회사가 연도 일괄 지급 정책일 때) */
	public void runCalendarAnnualGrant() throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		int year = cal.get(Calendar.YEAR);
		leaveService.grantAnnualByCalendarYear(year);
	}
}
