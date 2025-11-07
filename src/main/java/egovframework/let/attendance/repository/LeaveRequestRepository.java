package egovframework.let.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String>, LeaveRequestRepositoryCustom {
	List<LeaveRequest> findByEmpIdOrderByStartDateDesc(String empId);
}
