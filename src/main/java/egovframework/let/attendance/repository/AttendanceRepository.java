package egovframework.let.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
	List<Attendance> findByEmpId(String empId);

	List<Attendance> findByWorkDate(LocalDate workDate);
}
