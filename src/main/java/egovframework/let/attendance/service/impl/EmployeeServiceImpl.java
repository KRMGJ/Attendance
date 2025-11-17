package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Enums.FULL_TIME;
import static egovframework.let.attendance.common.Enums.INTERN;
import static egovframework.let.attendance.common.Enums.PART_TIME;
import static egovframework.let.attendance.common.Utils.formatDateOnly;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.AdminEmployeeSearch;
import egovframework.let.attendance.dto.request.EditEmployeeDto;
import egovframework.let.attendance.dto.request.RegistEmployeeDto;
import egovframework.let.attendance.dto.response.EmployeeViewDto;
import egovframework.let.attendance.entity.Employee;
import egovframework.let.attendance.repository.EmployeeRepository;
import egovframework.let.attendance.repository.mybatis.EmployeeDAO;
import egovframework.let.attendance.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("employeeService")
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

	@Resource(name = "employeeRepository")
	private EmployeeRepository employeeRepository;

	@Resource(name = "employeeDAO")
	private EmployeeDAO employeeDAO;

	@Resource(name = "passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "employeeIdGnrService")
	private EgovIdGnrService employeeIdGnrService;

	/**
	 * 직원 등록
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(RegistEmployeeDto dto) throws Exception {
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

			Employee employee = Employee.builder().id(employeeIdGnrService.getNextStringId()).name(dto.getName())
					.email(dto.getEmail()).employeeNumber(dto.getEmployeeNumber()).password(encodedPassword)
					.department(dto.getDepartment()).position(dto.getPosition()).employmentType(type)
					.hireDate(dto.getHireDate()).phone(dto.getPhone())
					.emergencyContactPhone(dto.getEmergencyContactPhone()).address(dto.getAddress())
					.workStartTime(dto.getWorkStartTime()).workEndTime(dto.getWorkEndTime()).build();
			employeeRepository.save(employee);

			jdbcTemplate.update("INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, ENABLED) VALUES (?, ?, ?, 1)",
					dto.getEmail(), encodedPassword, dto.getEmail());
			jdbcTemplate.update("INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES (?, ?)", dto.getEmail(),
					"ROLE_USER");
		} catch (Exception e) {
			log.error("Error registering employee: {}", e);
			throw e;
		}
	}

	/**
	 * 전체 직원 조회
	 */
	@Override
	public Page<EmployeeViewDto> list(AdminEmployeeSearch cond) throws Exception {
		try {
			Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
			Page<Employee> result = employeeRepository.searchForAdmin(cond, pageable);

			return result.map(emp -> EmployeeViewDto.builder().id(emp.getId()).name(emp.getName()).email(emp.getEmail())
					.employmentType(emp.getEmploymentType()).department(emp.getDepartment()).position(emp.getPosition())
					.status(emp.getStatus()).hireDate(formatDateOnly(emp.getHireDate()))
					.resignDate(formatDateOnly(emp.getResignDate())).build());
		} catch (Exception e) {
			log.error("Error fetching employee list for admin", e);
			throw e;
		}
	}

	/**
	 * 직원 상세 조회
	 */
	@Override
	public EmployeeViewDto getEmployeeDetail(String id) throws Exception {
		EmployeeViewDto employee = null;
		try {
			Employee emp = employeeRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("직원 정보를 찾을 수 없음: " + id));
			employee = EmployeeViewDto.builder().id(emp.getId()).name(emp.getName()).email(emp.getEmail())
					.employeeNumber(emp.getEmployeeNumber()).phone(emp.getPhone())
					.emergencyContactPhone(emp.getEmergencyContactPhone()).address(emp.getAddress())
					.workStartTime(emp.getWorkStartTime()).workEndTime(emp.getWorkEndTime())
					.department(emp.getDepartment()).password(emp.getPassword()).position(emp.getPosition())
					.employmentType(emp.getEmploymentType()).status(emp.getStatus())
					.hireDate(formatDateOnly(emp.getHireDate())).resignDate(formatDateOnly(emp.getResignDate()))
					.updatedAt(formatDateOnly(emp.getUpdatedAt())).build();
		} catch (Exception e) {
			log.error("Error retrieving employee detail for ID {}: {}", id, e);
			throw e;
		}
		return employee;
	}

	/**
	 * 직원 뷰 로드
	 */
	@Override
	public EmployeeViewDto loadView(String id) throws Exception {
		try {
			Employee emp = employeeRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("직원 정보를 찾을 수 없음: " + id));
			return EmployeeViewDto.builder().id(emp.getId()).name(emp.getName()).email(emp.getEmail())
					.employeeNumber(emp.getEmployeeNumber()).phone(emp.getPhone())
					.emergencyContactPhone(emp.getEmergencyContactPhone()).address(emp.getAddress())
					.workStartTime(emp.getWorkStartTime()).workEndTime(emp.getWorkEndTime())
					.department(emp.getDepartment()).password(emp.getPassword()).position(emp.getPosition())
					.employmentType(emp.getEmploymentType()).status(emp.getStatus())
					.hireDate(formatDateOnly(emp.getHireDate())).resignDate(formatDateOnly(emp.getResignDate()))
					.updatedAt(formatDateOnly(emp.getUpdatedAt())).build();
		} catch (Exception e) {
			log.error("Error loading employee view for ID {}: {}", id, e);
			throw e;
		}
	}

	/**
	 * 직원 정보 수정
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void edit(EditEmployeeDto dto, String actorUsername) throws Exception {
		try {
			Employee emp = employeeRepository.findById(dto.getId())
					.orElseThrow(() -> new IllegalArgumentException("직원 없음"));
			if (employeeDAO.canEdit(dto.getId(), actorUsername) == 0) {
				throw new SecurityException("수정 권한 없음");
			}

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
			dto.setEmploymentType(type);
			emp.setName(dto.getName());
			emp.setEmail(dto.getEmail());
			emp.setEmployeeNumber(dto.getEmployeeNumber());
			emp.setDepartment(dto.getDepartment());
			emp.setPosition(dto.getPosition());
			emp.setHireDate(dto.getHireDate());
			emp.setPhone(dto.getPhone());
			emp.setEmergencyContactPhone(dto.getEmergencyContactPhone());
			emp.setAddress(dto.getAddress());
			emp.setWorkStartTime(dto.getWorkStartTime());
			emp.setWorkEndTime(dto.getWorkEndTime());

			if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
				emp.setPassword(passwordEncoder.encode(dto.getPassword()));
			}

			employeeRepository.save(emp);
		} catch (Exception e) {
			log.error("Error editing employee ID {}: {}", dto.getId(), e);
			throw e;
		}
	}
}
