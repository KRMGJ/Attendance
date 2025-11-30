package egovframework.let.attendance.common;

public class ColorUtil {

	/** 근무 상태에 따른 색상 반환 */
	public static String workStatusColor(String status) {
		if (status == null) {
			return "text-gray-500";
		}

		switch (status) {
		case Enums.PRESENT:
			return "text-green-600";
		case Enums.LATE:
			return "text-yellow-600";
		case Enums.EARLY_LEAVE:
			return "text-blue-600";
		case Enums.ABSENT:
			return "text-red-600";
		default:
			return "text-gray-500";
		}
	}

	/** 근무 상태에 따른 아이콘 반환 */
	public static String workStatusIcon(String status) {
		if (status == null) {
			return "-";
		}

		switch (status) {
		case Enums.PRESENT:
			return "✔";
		case Enums.LATE:
			return "⚠";
		case Enums.EARLY_LEAVE:
			return "⤵";
		case Enums.ABSENT:
			return "✖";
		default:
			return "-";
		}
	}

	/** 고용 형태에 따른 색상 반환 */
	public static String employmentTypeColor(String type) {
		if (type == null) {
			return "text-gray-500";
		}

		switch (type) {
		case Enums.FULL_TIME:
			return "text-blue-700";
		case Enums.PART_TIME:
			return "text-purple-600";
		case Enums.INTERN:
			return "text-emerald-600";
		default:
			return "text-gray-500";
		}
	}

	/** 고용 형태에 따른 아이콘 반환 */
	public static String employmentTypeIcon(String type) {
		if (type == null) {
			return "-";
		}

		switch (type) {
		case Enums.FULL_TIME:
			return "👔";
		case Enums.PART_TIME:
			return "🕓";
		case Enums.INTERN:
			return "🌱";
		default:
			return "-";
		}
	}

	/** 고용 상태에 따른 색상 반환 */
	public static String employmentStatusColor(String status) {
		if (status == null) {
			return "text-gray-500";
		}

		switch (status) {
		case Enums.ACTIVE:
			return "text-green-700";
		case Enums.ON_LEAVE:
			return "text-yellow-700";
		case Enums.RESIGNED:
			return "text-gray-500";
		default:
			return "text-gray-500";
		}
	}

	/** 고용 상태에 따른 아이콘 반환 */
	public static String employmentStatusIcon(String status) {
		if (status == null) {
			return "-";
		}

		switch (status) {
		case Enums.ACTIVE:
			return "●";
		case Enums.ON_LEAVE:
			return "⏸";
		case Enums.RESIGNED:
			return "■";
		default:
			return "-";
		}
	}

	/** 휴가 유형에 따른 색상 반환 */
	public static String leaveTypeColor(String type) {
		if (type == null) {
			return "text-gray-500";
		}

		switch (type) {
		case Enums.ANNUAL:
			return "text-blue-600";
		case Enums.SICK:
			return "text-red-600";
		default:
			return "text-gray-500";
		}
	}

	/** 휴가 유형에 따른 아이콘 반환 */
	public static String leaveTypeIcon(String type) {
		if (type == null) {
			return "-";
		}

		switch (type) {
		case Enums.ANNUAL:
			return "🌴";
		case Enums.SICK:
			return "🤒";
		default:
			return "-";
		}
	}

	/** 휴가 상태에 따른 색상 반환 */
	public static String leaveStatusColor(String status) {
		if (status == null) {
			return "text-gray-500";
		}

		switch (status) {
		case Enums.PENDING:
			return "text-yellow-600";
		case Enums.APPROVED:
			return "text-green-600";
		case Enums.REJECTED:
			return "text-red-600";
		case Enums.CANCELED:
			return "text-gray-500";
		default:
			return "text-gray-500";
		}
	}

	/** 휴가 상태에 따른 아이콘 반환 */
	public static String leaveStatusIcon(String status) {
		if (status == null) {
			return "-";
		}

		switch (status) {
		case Enums.PENDING:
			return "⏳";
		case Enums.APPROVED:
			return "✔";
		case Enums.REJECTED:
			return "✖";
		case Enums.CANCELED:
			return "↩";
		default:
			return "-";
		}
	}

	/** 연차 지급 사유에 따른 색상 반환 */
	public static String grantReasonColor(String reason) {
		if (reason == null) {
			return "text-gray-500";
		}

		switch (reason) {
		case Enums.ANNUAL_GRANT_CALENDAR:
			return "text-blue-600";
		case Enums.ANNUAL_GRANT_ANNIVERSARY:
			return "text-indigo-600";
		case Enums.MONTHLY_ACCRUAL:
			return "text-emerald-600";
		default:
			return "text-gray-500";
		}
	}

	/** 연차 지급 사유에 따른 아이콘 반환 */
	public static String grantReasonIcon(String reason) {
		if (reason == null) {
			return "-";
		}

		switch (reason) {
		case Enums.ANNUAL_GRANT_CALENDAR:
			return "🗓";
		case Enums.ANNUAL_GRANT_ANNIVERSARY:
			return "🎉";
		case Enums.MONTHLY_ACCRUAL:
			return "📈";
		default:
			return "-";
		}
	}
}
