package egovframework.let.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveRequest;

@Repository("leaveRequestRepository")
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String>, LeaveRequestRepositoryCustom {
	List<LeaveRequest> findByEmpIdOrderByIdDesc(String empId);

	Optional<LeaveRequest> findByIdAndEmpId(String id, String empId);

	List<LeaveRequest> findByStatusOrderByCreatedAtAsc(String status);
}
