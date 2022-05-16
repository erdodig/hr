package hu.webuni.hr.dodi.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestByExample;
import hu.webuni.hr.dodi.model.HolidayRequestState;
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
	
	@PreAuthorize("#holidayRequestDto.requestingEmployee.username == authentication.name")
	@PostMapping
	public HolidayRequestDto createOrModifyHolidayRequest(@RequestBody @Valid HolidayRequestDto holidayRequestDto) {
		
		if (holidayRequestDto.getFromDate().isAfter(holidayRequestDto.getToDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		if (holidayRequestDto.getId() == null) 
				return holidayRequestMapper.holidayRequestToDto(holidayRequestService
						.save(holidayRequestMapper.dtoToHolidayRequest(holidayRequestDto)));
		else {
			
			if (holidayRequestService.findById(holidayRequestDto.getId()) == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
			return holidayRequestMapper.holidayRequestToDto(holidayRequestService
					.update(holidayRequestMapper.dtoToHolidayRequest(holidayRequestDto)));
		}
	}

	@PreAuthorize("#holidayRequestDto.leader.username == authentication.name")
	@PutMapping("/{id}")
	public HolidayRequestDto allowedHolidayRequest(@PathVariable long id,  @RequestParam boolean allowed
			, @RequestBody HolidayRequestDto holidayRequestDto) {
		
		HolidayRequest holidayRequest = getHolidayRequest(id);
				
		if (allowed)
			holidayRequest.setHolidayRequestState(HolidayRequestState.ALLOWED);
		else
			holidayRequest.setHolidayRequestState(HolidayRequestState.DENIED);
		
		return holidayRequestMapper.holidayRequestToDto(holidayRequestService.update(holidayRequest));
		
	}

//	@PreAuthorize("#holidayRequestDto.requestingEmployee.username == authentication.name")
	@DeleteMapping("/{id}")
	public void deleteHolidayRequest(@PathVariable long id/*, @RequestBody HolidayRequestDto holidayRequestDto*/) {
		
		HolidayRequest holidayRequest = getHolidayRequest(id);
		
		if (!holidayRequest.getHolidayRequestState().equals(HolidayRequestState.REQUEST))		
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		holidayRequestService.delete(id);
	}
	
	private HolidayRequest getHolidayRequest(long id) {
		
		HolidayRequest holidayRequest = holidayRequestService.findById(id);
		
		if (holidayRequest == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		return holidayRequest;
	}
		
	@PostMapping("/byExample/{page}/{count}")
	public List<HolidayRequestDto> findAllHolidayRequestByExample(@PathVariable Map<String, String> pathVars, 
			@RequestBody HolidayRequestByExample holidayRequestByExample) {
		
		String page = pathVars.get("page");
		String count = pathVars.get("count");
		
		if (page == null)
			page = "0";
		
		if (count == null)
			count = "5";
		
		Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(count));
			
		return holidayRequestMapper.holidayRequestToDtos(holidayRequestService
				.findHolidayRequestByExample(holidayRequestByExample, pageable));
	}
	
}
