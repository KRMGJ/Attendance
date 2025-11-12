package egovframework.let.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.entity.LeaveRequest;

public interface LeaveRequestRepositoryCustom {
	Page<LeaveRequest> searchMine(String empId, Pageable pageable);
}
