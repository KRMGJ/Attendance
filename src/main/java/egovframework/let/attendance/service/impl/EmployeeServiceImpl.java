package egovframework.let.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.RegistEmployeeDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(transactionManager = "jpaTxManager")
	public void register(RegistEmployeeDto dto) throws Exception {
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		Employee employee = Employee.builder().name(dto.getName()).email(dto.getEmail()).password(encodedPassword)
				.position(dto.getPosition()).employmentType(dto.getEmploymentType()).build();
		employeeRepository.save(employee);
	}
}
