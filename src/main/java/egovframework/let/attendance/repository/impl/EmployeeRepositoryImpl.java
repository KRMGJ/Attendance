package egovframework.let.attendance.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepositoryCustom;
import egovframework.let.attendance.repository.mybatis.EmployeeDAO;

@Repository("employeeRepositryImpl")
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

	@Resource(name = "employeeDAO")
	private EmployeeDAO employeeDAO;

	@Override
	public Page<Employee> searchForAdmin(AdminEmployeeSearch cond, Pageable pageable) {
		String kw = (cond.getQ() == null || cond.getQ().trim().isEmpty()) ? null
				: "%" + cond.getQ().trim().toLowerCase() + "%";
		String dept = (cond.getDept() == null || cond.getDept().trim().isEmpty()) ? null : cond.getDept().trim();

		String status = (cond.getStatus() == null || cond.getStatus().trim().isEmpty()) ? null
				: cond.getStatus().trim();

		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		int total = employeeDAO.countList(kw, dept, status);
		List<Employee> content = total == 0 ? Collections.emptyList()
				: employeeDAO.selectListPage(kw, dept, status, offset, limit);

		return new PageImpl<>(content, pageable, total);
	}
}
