package egovframework.let.attendance.service.impl;

import java.time.Year;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.let.attendance.entity.LeaveBalance;
import egovframework.let.attendance.repository.LeaveRepository;
import egovframework.let.attendance.service.LeaveService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService {
	@Autowired
	private LeaveRepository leaveRepository;

	/**
	 * 남은 휴가 일수 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public LeaveBalance getRemaining(String empId) {
		int currentYear = Year.now().getValue();
		Optional<LeaveBalance> leaveBalance = null;
		try {
			leaveBalance = leaveRepository.findByEmpIdAndYear(empId, currentYear);
		} catch (Exception e) {
			log.error("Error fetching leave balance for empId {}: {}", empId, e.getMessage());
		}
		return leaveBalance
				.orElseGet(() -> LeaveBalance.builder().empId(empId).year(currentYear).total(0).used(0).build());
	}

}
