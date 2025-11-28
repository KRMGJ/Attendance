package egovframework.let.attendance.common;

public class LabelUtil {

	public static String label(String code) {
		if (code == null) {
			return "-";
		}

		switch (code) {

		case Enums.FULL_TIME:
			return Enums.Label.FULL_TIME;
		case Enums.PART_TIME:
			return Enums.Label.PART_TIME;
		case Enums.INTERN:
			return Enums.Label.INTERN;

		case Enums.ACTIVE:
			return Enums.Label.ACTIVE;
		case Enums.ON_LEAVE:
			return Enums.Label.ON_LEAVE;
		case Enums.RESIGNED:
			return Enums.Label.RESIGNED;

		case Enums.PRESENT:
			return Enums.Label.PRESENT;
		case Enums.LATE:
			return Enums.Label.LATE;
		case Enums.EARLY_LEAVE:
			return Enums.Label.EARLY_LEAVE;
		case Enums.ABSENT:
			return Enums.Label.ABSENT;

		case Enums.ANNUAL:
			return Enums.Label.ANNUAL;
		case Enums.SICK:
			return Enums.Label.SICK;

		case Enums.PENDING:
			return Enums.Label.PENDING;
		case Enums.APPROVED:
			return Enums.Label.APPROVED;
		case Enums.REJECTED:
			return Enums.Label.REJECTED;
		case Enums.CANCELED:
			return Enums.Label.CANCELED;

		case Enums.ANNUAL_GRANT_CALENDAR:
			return Enums.Label.ANNUAL_GRANT_CALENDAR;
		case Enums.ANNUAL_GRANT_ANNIVERSARY:
			return Enums.Label.ANNUAL_GRANT_ANNIVERSARY;
		case Enums.MONTHLY_ACCRUAL:
			return Enums.Label.MONTHLY_ACCRUAL;

		default:
			return "-";
		}
	}
}
