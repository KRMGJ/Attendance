package egovframework.let.attendance.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

	public static String formatDate(LocalDateTime date) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(fmt);
	}
}
