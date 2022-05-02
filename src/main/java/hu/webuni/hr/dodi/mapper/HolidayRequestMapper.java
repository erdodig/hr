package hu.webuni.hr.dodi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.model.HolidayRequest;

@Mapper(componentModel = "spring")
public interface HolidayRequestMapper {

	@Mapping(source = "requestingEmployee.id", target = "requestingEmployee.employeeId")
	@Mapping(source = "requestingEmployee.title", target = "requestingEmployee.position.name")
	@Mapping(source = "requestingEmployee.entryDate", target = "requestingEmployee.dateOfStartWork")
	@Mapping(source = "leader.id", target = "leader.employeeId")
	@Mapping(source = "leader.title", target = "leader.position.name")
	@Mapping(source = "leader.entryDate", target = "leader.dateOfStartWork")
	@Mapping(target = "requestingEmployee.company.companyType", ignore = true)
	@Mapping(target = "leader.company.companyType", ignore = true)
	HolidayRequest dtoToHolidayRequest(HolidayRequestDto holidayRequestDto);

	@InheritInverseConfiguration 
	HolidayRequestDto holidayRequestToDto(HolidayRequest holidayRequest);
	
	List<HolidayRequestDto> holidayRequestToDtos(List<HolidayRequest> holidayRequests);
	
}
