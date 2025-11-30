package egovframework.let.attendance.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee", uniqueConstraints = {
		@UniqueConstraint(name = "uk_employee_email", columnNames = { "email" }),
		@UniqueConstraint(name = "uk_employee_number", columnNames = { "employee_number" }) }, indexes = {
				@Index(name = "ix_employee_hiredate", columnList = "hire_date") })
@org.hibernate.annotations.Table(appliesTo = "employee", comment = "직원 정보 테이블")
public class Employee {

	@Id
	@Column(length = 36)
	@Comment("직원 식별자(UUID)")
	private String id;

	@Column(name = "employee_number", length = 20, columnDefinition = "VARCHAR(20) check (regexp_like(employee_number, '^[A-Z0-9]+$'))")
	@Comment("직원 번호")
	private String employeeNumber;

	@Column(name = "name", length = 80, nullable = false)
	@Comment("직원 이름")
	private String name;

	@Column(name = "email", length = 120, columnDefinition = "VARCHAR(120) check (regexp_like(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$') )", unique = true, nullable = false)
	@Comment("직원 이메일(로그인 ID)")
	private String email;

	@Column(name = "password", nullable = false)
	@Comment("암호화된 비밀번호")
	private String password;

	@Column(name = "position", length = 60)
	@Comment("직급 또는 직책")
	private String position;

	@Column(name = "department", length = 50)
	@Comment("부서 이름")
	private String department;

	@Column(name = "phone", length = 20, columnDefinition = "VARCHAR(20) check (regexp_like(phone, '^[0-9\\-+() ]+$'))")
	@Comment("전화번호")
	private String phone;

	@Column(name = "emergency_contact_phone", length = 20, columnDefinition = "VARCHAR(20) check (regexp_like(emergency_contact_phone, '^[0-9\\-+() ]+$'))")
	@Comment("비상 연락처 전화번호")
	private String emergencyContactPhone;

	@Column(name = "address", length = 200)
	@Comment("주소")
	private String address;

	@Column(name = "work_start_time", columnDefinition = "VARCHAR(5) check (regexp_like(work_start_time, '^(?:[01]\\d|2[0-3]):[0-5]\\d$'))")
	@Comment("근무 시작 시간 (HH:mm 형식)")
	private String workStartTime;

	@Column(name = "work_end_time", columnDefinition = "VARCHAR(5) check (regexp_like(work_end_time, '^(?:[01]\\d|2[0-3]):[0-5]\\d$'))")
	@Comment("근무 종료 시간 (HH:mm 형식)")
	private String workEndTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Builder.Default
	@Column(name = "hire_date", nullable = false)
	@Comment("입사일")
	private Date hireDate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "resign_date")
	@Comment("퇴사일")
	private Date resignDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	@Comment("정보 수정일")
	private Date updatedAt;

	@Column(name = "employment_type", length = 20, nullable = false)
	@Comment("고용 형태(정규직, 계약직 등)")
	private String employmentType;

	@Builder.Default
	@Column(name = "status", length = 20, nullable = false)
	@Comment("고용 상태(재직중, 휴직중, 퇴사 등)")
	private String status = "재직 중";

	@Builder.Default
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<Attendance> attendances = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<LeaveRequest> leaveRequests = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<LeaveBalance> leaveBalances = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<LeaveGrantLog> leaveGrantLogs = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		if (hireDate == null) {
			hireDate = new Date();
		}
		if (workStartTime == null) {
			workStartTime = "09:00";
		}
		if (workEndTime == null) {
			workEndTime = "18:00";
		}
		if (status == null) {
			status = "재직 중";
		}
		updatedAt = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = new Date();
	}
}
