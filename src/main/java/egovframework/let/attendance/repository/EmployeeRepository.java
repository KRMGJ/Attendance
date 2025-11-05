package egovframework.let.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
	Optional<Employee> findByEmail(String email);
}
