package egovframework.let.attendance.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String>, AttendanceRepositoryCustom {
	Optional<Attendance> findByEmpIdAndWorkDate(String empId, Date workDate);

	boolean existsByEmpIdAndWorkDate(String empId, Date workDate);

	List<Attendance> findTop7ByEmpIdOrderByWorkDateDesc(String empId);

	List<Attendance> findTop100ByEmpIdOrderByWorkDateDesc(String empId);
}
