package egovframework.let.attendance.repository.impl;

import static egovframework.let.attendance.common.Enums.PENDING;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveRequest;
import egovframework.let.attendance.repository.LeaveRequestRepositoryCustom;
import egovframework.let.attendance.repository.mybatis.LeaveRequestDAO;

@Repository("leaveRequestRepositoryImpl")
public class LeaveRequestRepositoryImpl implements LeaveRequestRepositoryCustom {

	@Resource(name = "leaveRequestDAO")
	private LeaveRequestDAO leaveRequestDAO;

	@Override
	public Page<LeaveRequest> searchMine(String empId, Pageable pageable) {
		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		long total = leaveRequestDAO.countMine(empId);
		List<LeaveRequest> content = total == 0 ? Collections.emptyList()
				: leaveRequestDAO.selectMinePage(empId, offset, limit);

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<LeaveRequest> searchPending(Pageable pageable) {
		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();
		long total = leaveRequestDAO.countByStatus(PENDING);
		List<LeaveRequest> content = total == 0 ? Collections.emptyList()
				: leaveRequestDAO.selectByStatusPage(PENDING, offset, limit);
		return new PageImpl<>(content, pageable, total);
	}

}
