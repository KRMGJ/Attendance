package egovframework.let.attendance.common;

public interface Enums {
	/** 고용 형태 */
	public static String FULL_TIME = "정규직";
	public static String PART_TIME = "계약직";
	public static String INTERN = "인턴";

	/** 고용 상태 */
	public static String ACTIVE = "재직 중";
	public static String ON_LEAVE = "휴직 중";
	public static String RESIGNED = "퇴사";

	/** 근무 상태 */
	public static String PRESENT = "정상 출근";
	public static String LATE = "지각";
	public static String EARLY_LEAVE = "조퇴";
	public static String ABSENT = "결근";

	/** 휴가 유형 */
	public static String ANNUAL = "연차";
	public static String SICK = "병가";

	/** 휴가 상태 */
	public static String PENDING = "승인 대기";
	public static String APPROVED = "승인 완료";
	public static String REJECTED = "반려";
	public static String CANCELED = "취소";

	/** 연차 지급 사유 */
	public static String ANNUAL_GRANT_CALENDAR = "캘린더 기준 연차 지급";
	public static String ANNUAL_GRANT_ANNIVERSARY = "입사기념일 기준 연차 지급";
	public static String MONTHLY_ACCRUAL = "월만근 연차 지급";

}
