package egovframework.let.attendance.common;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {

	/**
	 * Date 객체를 "yyyy-MM-dd HH:mm:ss" 형식의 문자열로 변환
	 * 
	 * @param date 변환할 Date 객체
	 * @return 형식화된 날짜 문자열
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().format(fmt);
	}

	/**
	 * Date 객체를 "yyyy-MM-dd" 형식의 문자열로 변환
	 * 
	 * @param date 변환할 Date 객체
	 * @return 형식화된 날짜 문자열
	 */
	public static String formatDateOnly(Date date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().format(fmt);
	}
}
