package egovframework.let.attendance.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepositoryCustom;
import egovframework.let.attendance.repository.mybatis.EmployeeDAO;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public Page<Employee> searchForAdmin(AdminEmployeeSearch cond, Pageable pageable) {
		String kw = (cond.getQ() == null || cond.getQ().trim().isEmpty()) ? null
				: "%" + cond.getQ().trim().toLowerCase() + "%";
		String dept = cond.getDept();
		String status = cond.getStatus();

		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		long total = employeeDAO.countList(kw, dept, status);
		List<Employee> content = total == 0 ? Collections.emptyList()
				: employeeDAO.selectListPage(kw, dept, status, offset, limit);

		return new PageImpl<>(content, pageable, total);
	}
}
