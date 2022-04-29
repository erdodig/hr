package hu.webuni.hr.dodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.HolidayRequestRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@Service
public class HolidayRequestService {

	@Autowired
	private HolidayRequestRepository holidayRequestRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	public HolidayRequest save(HolidayRequest holidayRequest) {
		return holidayRequestRepository.save(holidayRequest);
	}
	
	public HolidayRequest update(HolidayRequest holidayRequest) {
		
		if (!holidayRequestRepository.existsById(holidayRequest.getId()))
			return null;
		
		return holidayRequestRepository.save(holidayRequest);
	}
	
	public void deleteAll() {
		holidayRequestRepository.deleteAll();
	}

	public HolidayRequest findByIdAndPositionWithEmployees(long id) {
		
//		HolidayRequest holidayRequest = holidayRequestRepository.findByIdAndPosition(id).get();
		HolidayRequest holidayRequest = holidayRequestRepository.getHolidayRequestAndPositionWithEmployees(id);
		
		if (holidayRequest == null)
			return null;
		
//		List<Employee> requestEmployees = employeeRepository.findByPosition(holidayRequest.getRequestingEmployee().getPosition());
//		List<Employee> leaderEmployees = employeeRepository.findByPosition(holidayRequest.getLeader().getPosition());
//		
//		holidayRequest.getRequestingEmployee().getPosition().setEmployees(requestEmployees);
//		holidayRequest.getLeader().getPosition().setEmployees(leaderEmployees);
		
//		holidayRequest.getRequestingEmployee().getPosition().getEmployees();
//		holidayRequest.getLeader().getPosition().getEmployees();
		
		return holidayRequest;
	}
	
}
