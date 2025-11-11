package egovframework.let.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.attendance.entity.SessionLimit;

@Repository
public interface SessionLimitRepository extends JpaRepository<SessionLimit, String> {
	Optional<SessionLimit> findBySessionId(String sessionId);
}
