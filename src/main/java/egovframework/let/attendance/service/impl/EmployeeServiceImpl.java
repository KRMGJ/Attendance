package egovframework.let.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 직원 등록
	 */
	@Override
	public void register(RegistEmployeeDto dto) {
		try {
			String encodedPassword = passwordEncoder.encode(dto.getPassword());

			Employee employee = Employee.builder().name(dto.getName()).email(dto.getEmail()).password(encodedPassword)
					.position(dto.getPosition()).employmentType(dto.getEmploymentType()).build();
			employeeRepository.save(employee);

			jdbcTemplate.update("INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (?, ?, ?, 1)",
					dto.getEmail(), encodedPassword, dto.getEmail());
			jdbcTemplate.update("INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES (?, ?)", dto.getEmail(),
					"ROLE_USER");
		} catch (Exception e) {
			log.error("Error registering employee: {}", e.getMessage());
		}
	}
}
