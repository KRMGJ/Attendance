package egovframework.let.attendance.common;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

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

	/**
	 * PaginationInfo 생성
	 * 
	 * @param page  현재 페이지 번호
	 * @param size  페이지당 레코드 수
	 * @param total 전체 레코드 수
	 */
	public static PaginationInfo buildPi(int page, int size, int total) {
		PaginationInfo pi = new PaginationInfo();
		pi.setCurrentPageNo(page);
		pi.setRecordCountPerPage(size);
		pi.setPageSize(10);
		pi.setTotalRecordCount(total);
		return pi;
	}
}
