package hu.webuni.hr.dodi.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.mapper.HolidayRequestMapper;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestState;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.repository.PositionRepository;
import hu.webuni.hr.dodi.service.HolidayRequestService;

@RestController
@RequestMapping("/api/holidays")
public class HolidayRequestController {
	
	@Autowired
	HolidayRequestService holidayRequestService;
	
	@Autowired
	HolidayRequestMapper holidayRequestMapper;
	
	@Autowired
	PositionRepository positionRepository;
	
	@PostMapping
	public HolidayRequestDto createHolidayRequest(@RequestBody @Valid HolidayRequestDto holidayRequestDto ) {
		
		if (holidayRequestDto.getFromDate().isAfter(holidayRequestDto.getToDate()))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		return holidayRequestMapper.holidayRequestToDto(holidayRequestService
				.save(holidayRequestMapper.dtoToHolidayRequest(holidayRequestDto)));
	}

	@PutMapping("/{id}")
	public HolidayRequest allowedHolidayRequest(@PathVariable long id,  @RequestParam boolean allowed) {
		
		HolidayRequest holidayRequest = holidayRequestService.findByIdAndPositionWithEmployees(id);
		
		if (holidayRequest == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		if (allowed)
			holidayRequest.setHolidayRequestState(HolidayRequestState.ALLOWED);
		else
			holidayRequest.setHolidayRequestState(HolidayRequestState.DENIED);
		
		return holidayRequestService.update(holidayRequest);
		
	}
	
}
