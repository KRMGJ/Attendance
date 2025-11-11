package egovframework.let.attendance.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import egovframework.let.attendance.repository.LeaveRequestRepositoryCustom;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

}
