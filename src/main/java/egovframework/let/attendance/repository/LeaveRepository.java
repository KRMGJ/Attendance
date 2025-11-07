package egovframework.let.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveBalance;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveBalance, String> {
	Optional<LeaveBalance> findByEmpIdAndYear(String empId, int year);

}
