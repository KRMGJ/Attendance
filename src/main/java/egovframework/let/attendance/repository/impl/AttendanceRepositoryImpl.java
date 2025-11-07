package egovframework.let.attendance.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.repository.AttendanceRepositoryCustom;

public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Attendance> searchForAdmin(AdminAttendanceSearch cond, Pageable pageable) {
		StringBuilder jpql = new StringBuilder(
				"select a from Attendance a " + "join fetch a.employee e " + "where 1=1 ");
		StringBuilder countJpql = new StringBuilder(
				"select count(a) from Attendance a " + "join a.employee e " + "where 1=1 ");

		Map<String, Object> params = new HashMap<>();

		if (cond.getQ() != null && !cond.getQ().isEmpty()) {
			jpql.append("and (lower(e.name) like :kw or lower(e.email) like :kw) ");
			countJpql.append("and (lower(e.name) like :kw or lower(e.email) like :kw) ");
			params.put("kw", "%" + cond.getQ().toLowerCase() + "%");
		}
		Date from = cond.getFrom();
		if (from != null) {
			jpql.append("and a.workDate >= :from ");
			countJpql.append("and a.workDate >= :from ");
			params.put("from", from);
		}
		Date to = cond.getTo();
		if (to != null) {
			jpql.append("and a.workDate <= :to ");
			countJpql.append("and a.workDate <= :to ");
			params.put("to", to);
		}

		jpql.append("order by a.workDate desc, e.name asc");

		TypedQuery<Attendance> dataQuery = entityManager.createQuery(jpql.toString(), Attendance.class);
		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

		params.forEach((key, value) -> {
			dataQuery.setParameter(key, value);
			countQuery.setParameter(key, value);
		});

		dataQuery.setFirstResult((int) pageable.getOffset());
		dataQuery.setMaxResults(pageable.getPageSize());

		List<Attendance> content = dataQuery.getResultList();
		Long total = countQuery.getSingleResult();
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public List<Attendance> findMyRange(String empId, Date from, Date to) {
		StringBuilder jpql = new StringBuilder("select a from Attendance a " + "join fetch a.employee e "
				+ "where e.empId = :empId " + "and a.workDate between :from and :to " + "order by a.workDate asc");
		TypedQuery<Attendance> query = entityManager.createQuery(jpql.toString(), Attendance.class);
		query.setParameter("empId", empId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
	}
}
