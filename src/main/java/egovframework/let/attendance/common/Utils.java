package egovframework.let.attendance.common;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().format(fmt);
	}

	public static String formatDateOnly(Date date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().format(fmt);
	}
}
