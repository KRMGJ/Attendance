package egovframework.let.attendance.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.repository.AttendanceRepositoryCustom;
import egovframework.let.attendance.repository.mybatis.AttendanceDAO;

public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AttendanceDAO attendanceDAO;

	@Override
	public Page<Attendance> searchForAdmin(AdminAttendanceSearch cond, Pageable pageable) {
		String kw = (cond.getQ() == null || cond.getQ().trim().isEmpty()) ? null
				: "%" + cond.getQ().toLowerCase() + "%";
		Date from = cond.getFrom();
		Date to = cond.getTo();

		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		long total = attendanceDAO.countAdmin(kw, from, to);
		List<Attendance> content = total == 0 ? Collections.emptyList()
				: attendanceDAO.selectAdminPage(kw, from, to, offset, limit);

		return new PageImpl<>(content, pageable, total);
	}
}
