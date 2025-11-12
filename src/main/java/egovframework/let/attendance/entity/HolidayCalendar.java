package egovframework.let.attendance.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "holiday_calendar", uniqueConstraints = {
		@UniqueConstraint(name = "uk_holiday_day", columnNames = { "day" }) }, indexes = {
				@Index(name = "ix_holiday_workingday", columnList = "is_working_day") })
@org.hibernate.annotations.Table(appliesTo = "holiday_calendar", comment = "휴일 및 근무일 관리 테이블")
public class HolidayCalendar {
	@Id
	@Column(length = 36)
	@Comment("UUID")
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name = "day", nullable = false)
	@Comment("날짜")
	private Date day;

	@Column(name = "name", length = 100, nullable = false)
	@Comment("명칭")
	private String name;

	@Column(name = "is_working_day", nullable = false)
	@Comment("근무일여부 (0: 휴일, 1: 근무일)")
	private Integer isWorkingDay;

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
	}
}
