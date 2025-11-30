package egovframework.let.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.LeaveGrantLog;

@Repository("leaveGrantLogRepository")
public interface LeaveGrantLogRepository extends JpaRepository<LeaveGrantLog, String>, LeaveGrantLogRepositoryCustom {

}
