package egovframework.let.attendance.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminGrantLogSearch;
import egovframework.let.attendance.repository.LeaveGrantLogRepositoryCustom;
import egovframework.let.attendance.repository.mybatis.LeaveGrantLogDAO;

public class LeaveGrantLogRepositoryCustomImpl implements LeaveGrantLogRepositoryCustom {

	@Resource(name = "leaveGrantLogDAO")
	private LeaveGrantLogDAO leaveGrantLogDAO;

	@Override
	public Page<Map<String, Object>> searchForAdmin(AdminGrantLogSearch cond, Pageable pageable) {
		String kw = (cond.getQ() == null || cond.getQ().trim().isEmpty()) ? null
				: "%" + cond.getQ().trim().toLowerCase() + "%";
		String reason = (cond.getReason() == null || cond.getReason().trim().isEmpty()) ? null
				: "%" + cond.getReason().trim().toLowerCase() + "%";
		String kind = cond.getKind();

		Integer year = cond.getYear();
		Date from = cond.getFrom();
		Date to = cond.getTo();
		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		long total = leaveGrantLogDAO.countLog(kw, reason, kind, year, from, to);
		List<Map<String, Object>> content = total == 0 ? Collections.emptyList()
				: leaveGrantLogDAO.selectLogPaged(kw, reason, kind, year, from, to, offset, limit);
		return new PageImpl<>(content, pageable, total);
	}

}
