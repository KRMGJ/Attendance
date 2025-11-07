package egovframework.let.attendance.service.impl;

import static egovframework.let.attendance.common.Utils.formatDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.dto.request.AdminAttendanceSearch;
import egovframework.let.attendance.dto.response.AttendanceListDto;
import egovframework.let.attendance.entity.Attendance;
import egovframework.let.attendance.repository.AttendanceRepository;
import egovframework.let.attendance.service.AdminService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<AttendanceListDto> list(AdminAttendanceSearch cond) {
		Page<AttendanceListDto> dtos = null;
		try {
			Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
			Page<Attendance> result = attendanceRepository.searchForAdmin(cond, pageable);
			dtos = result.map(attendance -> AttendanceListDto.builder().id(attendance.getId())
					.empId(attendance.getEmpId()).workDate(attendance.getWorkDate())
					.checkIn(formatDate(attendance.getCheckIn())).checkOut(formatDate(attendance.getCheckOut()))
					.status(attendance.getStatus()).overtimeMinutes(attendance.getOvertimeMinutes())
					.employee(attendance.getEmployee()).build());
		} catch (Exception e) {
			log.error("Error fetching attendance list for admin", e);
		}
		return dtos;
	}

}
