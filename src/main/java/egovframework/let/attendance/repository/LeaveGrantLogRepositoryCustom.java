package egovframework.let.attendance.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminGrantLogSearch;

public interface LeaveGrantLogRepositoryCustom {
	Page<Map<String, Object>> searchForAdmin(AdminGrantLogSearch cond, Pageable pageable);
}
