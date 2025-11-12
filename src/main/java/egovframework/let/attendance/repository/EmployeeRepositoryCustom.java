package egovframework.let.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.entity.Employee;

public interface EmployeeRepositoryCustom {
	Page<Employee> searchForAdmin(AdminEmployeeSearch cond, Pageable pageable);
}
