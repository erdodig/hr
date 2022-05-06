package hu.webuni.hr.dodi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(source = "employeeId", target = "id")
	@Mapping(source = "position.name", target = "title")
	@Mapping(source = "dateOfStartWork", target = "entryDate")
	@Mapping(target = "company.employees", ignore = true)
	@Mapping(source = "leader.employeeId", target = "leader.id")
	@Mapping(source = "leader.position.name", target = "leader.title")
	@Mapping(source = "leader.dateOfStartWork", target = "leader.entryDate")
	EmployeeDto employeeToDto(Employee employee);

	@InheritInverseConfiguration
	@Mapping(target = "company.companyType", ignore = true)
	@Mapping(target = "leader.company.companyType", ignore = true)
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	List<Employee> dtosToEmployees(List<EmployeeDto> employees);

}
