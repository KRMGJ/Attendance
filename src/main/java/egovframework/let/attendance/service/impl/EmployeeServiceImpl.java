package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Enums.FULL_TIME;
import static egovframework.let.attendance.common.Enums.INTERN;
import static egovframework.let.attendance.common.Enums.PART_TIME;
import static egovframework.let.attendance.common.Utils.formatDateOnly;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.repository.mybatis.EmployeeDAO;
import egovframework.let.attendance.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeDAO employeeDAO;

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
			String type = null;
			if (dto.getEmploymentType().equals("FULL_TIME")) {
				type = FULL_TIME;
			} else if (dto.getEmploymentType().equals("PART_TIME")) {
				type = PART_TIME;
			} else if (dto.getEmploymentType().equals("INTERN")) {
				type = INTERN;
			} else {
				throw new IllegalArgumentException("Invalid employment type: " + dto.getEmploymentType());
			}

			Employee employee = Employee.builder().name(dto.getName()).email(dto.getEmail()).password(encodedPassword)
					.position(dto.getPosition()).employmentType(type).build();
			employeeRepository.save(employee);

			jdbcTemplate.update("INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (?, ?, ?, 1)",
					dto.getEmail(), encodedPassword, dto.getEmail());
			jdbcTemplate.update("INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES (?, ?)", dto.getEmail(),
					"ROLE_USER");
		} catch (Exception e) {
			log.error("Error registering employee: {}", e.getMessage());
		}
	}

	/**
	 * 전체 직원 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Employee> getAllEmployees() {
		List<Employee> employees = null;
		try {
			employees = employeeRepository.findAllByOrderByHireDateDesc();
		} catch (Exception e) {
			log.error("Error retrieving all employees: {}", e.getMessage());
		}
		return employees;
	}

	/**
	 * 직원 상세 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public EmployeeViewDto getEmployeeDetail(String id) {
		EmployeeViewDto employee = null;
		try {
			Employee emp = employeeRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("직원 정보를 찾을 수 없음: " + id));
			employee = EmployeeViewDto.builder().id(emp.getId()).name(emp.getName()).email(emp.getEmail())
					.position(emp.getPosition()).employmentType(emp.getEmploymentType()).status(emp.getStatus())
					.hireDate(formatDateOnly(emp.getHireDate())).resignDate(formatDateOnly(emp.getResignDate()))
					.updatedAt(formatDateOnly(emp.getUpdatedAt())).build();
		} catch (Exception e) {
			log.error("Error retrieving employee detail for ID {}: {}", id, e.getMessage());
		}
		return employee;
	}

	/**
	 * 직원 뷰 로드
	 */
	@Override
	public EmployeeViewDto loadView(String id) {
		return employeeDAO.selectViewById(id);
	}

	/**
	 * 직원 정보 수정
	 */
	@Override
	public void edit(EditEmployeeDto dto, String actorUsername) {
		if (employeeDAO.canEdit(dto.getId(), actorUsername) == 0) {
			throw new SecurityException("수정 권한 없음");
		}

		// 이메일 중복 방지
		if (employeeDAO.existsByEmailExcludingId(dto.getEmail(), dto.getId()) > 0) {
			throw new IllegalArgumentException("이메일이 중복됩니다.");
		}

		String type = null;
		if (dto.getEmploymentType().equals("FULL_TIME")) {
			type = FULL_TIME;
		} else if (dto.getEmploymentType().equals("PART_TIME")) {
			type = PART_TIME;
		} else if (dto.getEmploymentType().equals("INTERN")) {
			type = INTERN;
		} else {
			throw new IllegalArgumentException("Invalid employment type: " + dto.getEmploymentType());
		}
		dto.setUpdatedAt(new Date());
		dto.setEmploymentType(type);
		employeeDAO.updateProfile(dto);

		// 비밀번호 변경은 입력이 있을 때만
		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
				throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
			}
			String encoded = passwordEncoder.encode(dto.getPassword());
			employeeDAO.updatePassword(dto.getId(), encoded, dto.getUpdatedAt());
		}

	}
}
