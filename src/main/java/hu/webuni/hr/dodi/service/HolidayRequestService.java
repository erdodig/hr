package hu.webuni.hr.dodi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestByExample;
import hu.webuni.hr.dodi.model.HolidayRequestState;
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

	public HolidayRequest findById(long id) {
		
		HolidayRequest holidayRequest = holidayRequestRepository.findById(id).get();
		
		if (holidayRequest == null)
			return null;
		
		return holidayRequest;
	}

	public void delete(long id) {
		holidayRequestRepository.deleteById(id);		
	}

	public List<HolidayRequest> findHolidayRequestByExample(HolidayRequestByExample holidayRequestByExample, 
			Pageable pageable) {

		HolidayRequestState state = holidayRequestByExample.getHolidayRequestState();
		String requestName = holidayRequestByExample.getRequestName();
		String leaderName = holidayRequestByExample.getLeaderName();
		LocalDateTime fromRequestTime = holidayRequestByExample.getFromRequestTime();
		LocalDateTime toRequestTime = holidayRequestByExample.getToRequestTime();
		LocalDate fromDate = holidayRequestByExample.getFromDate();
		LocalDate toDate = holidayRequestByExample.getToDate();
		String sorting = holidayRequestByExample.getSorting();
		String direction = holidayRequestByExample.getDirection();
				
		Specification<HolidayRequest> spec = Specification.where(null);
		
		if (state != null)
			spec = spec.and(HolidayRequestSpecifications.hasState(state));

		if (StringUtils.hasText(requestName))
			spec = spec.and(HolidayRequestSpecifications.hasRequestName(requestName));

		if (StringUtils.hasText(leaderName))
			spec = spec.and(HolidayRequestSpecifications.hasLeaderName(leaderName));
		
		if (fromRequestTime != null && toRequestTime != null) 
			spec = spec.and(HolidayRequestSpecifications.betweenRequestTime(fromRequestTime, toRequestTime));
		
		if (fromDate != null && toDate != null)
			spec = spec.and(HolidayRequestSpecifications.hasHolidayDate(fromDate, toDate));
		
		if (StringUtils.hasText(sorting)) {
			if (StringUtils.hasText(direction))
				return holidayRequestRepository.findAll(spec, Sort.by(direction.toLowerCase()
						.equals("desc")?Direction.DESC:Direction.ASC, sorting));
			else
				return holidayRequestRepository.findAll(spec, Sort.by(sorting));
		}
		
		Page<HolidayRequest> page = holidayRequestRepository.findAll(spec, pageable);
			
		return page.getContent();
	}	
	
}
