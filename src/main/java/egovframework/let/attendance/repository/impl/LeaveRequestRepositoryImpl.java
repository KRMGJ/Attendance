package egovframework.let.attendance.repository.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import egovframework.let.attendance.repository.LeaveRequestRepositoryCustom;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long countOverlap(String empId, Date start, Date end) {
		StringBuilder jpql = new StringBuilder(
				"select count(l) from LeaveRequest l " + "where l.empId = :empId and l.status <> 'REJECTED' "
						+ "and l.startDate <= :end and l.endDate >= :start");
		long count = entityManager.createQuery(jpql.toString(), Long.class).setParameter("empId", empId)
				.setParameter("start", start).setParameter("end", end).getSingleResult();
		return count;
	}

}
