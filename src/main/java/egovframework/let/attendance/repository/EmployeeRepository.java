package egovframework.let.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.Employee;

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, String>, EmployeeRepositoryCustom {
	Optional<Employee> findByEmail(String email);

	List<Employee> findAllByOrderByHireDateDesc();
}
