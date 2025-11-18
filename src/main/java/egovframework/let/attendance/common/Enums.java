package egovframework.let.attendance.common;

public interface Enums {

	/* ================= 코드 값 ================= */

	/** 고용 형태 코드 */
	String FULL_TIME = "FULL_TIME";
	String PART_TIME = "PART_TIME";
	String INTERN = "INTERN";

	/** 고용 상태 코드 */
	String ACTIVE = "ACTIVE";
	String ON_LEAVE = "ON_LEAVE";
	String RESIGNED = "RESIGNED";

	/** 근무 상태 코드 */
	String PRESENT = "PRESENT";
	String LATE = "LATE";
	String EARLY_LEAVE = "EARLY_LEAVE";
	String ABSENT = "ABSENT";

	/** 휴가 유형 코드 */
	String ANNUAL = "ANNUAL";
	String SICK = "SICK";

	/** 휴가 상태 코드 */
	String PENDING = "PENDING";
	String APPROVED = "APPROVED";
	String REJECTED = "REJECTED";
	String CANCELED = "CANCELED";

	/** 연차 지급 사유 코드 */
	String ANNUAL_GRANT_CALENDAR = "ANNUAL_GRANT_CALENDAR";
	String ANNUAL_GRANT_ANNIVERSARY = "ANNUAL_GRANT_ANNIVERSARY";
	String MONTHLY_ACCRUAL = "MONTHLY_ACCRUAL";

	/* =============== 화면 라벨(한글) =============== */

	interface Label {
		/** 고용 형태 라벨 */
		String FULL_TIME = "정규직";
		String PART_TIME = "계약직";
		String INTERN = "인턴";

		/** 고용 상태 라벨 */
		String ACTIVE = "재직 중";
		String ON_LEAVE = "휴직 중";
		String RESIGNED = "퇴사";

		/** 근무 상태 라벨 */
		String PRESENT = "정상 출근";
		String LATE = "지각";
		String EARLY_LEAVE = "조퇴";
		String ABSENT = "결근";

		/** 휴가 유형 라벨 */
		String ANNUAL = "연차";
		String SICK = "병가";

		/** 휴가 상태 라벨 */
		String PENDING = "승인 대기";
		String APPROVED = "승인 완료";
		String REJECTED = "반려";
		String CANCELED = "취소";

		/** 연차 지급 사유 라벨 */
		String ANNUAL_GRANT_CALENDAR = "캘린더 기준 연차 지급";
		String ANNUAL_GRANT_ANNIVERSARY = "입사기념일 기준 연차 지급";
		String MONTHLY_ACCRUAL = "월만근 연차 지급";
	}
}
