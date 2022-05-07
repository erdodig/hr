package hu.webuni.hr.dodi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(source = "id", target = "employeeId")
	@Mapping(source = "title", target = "position.name")
	@Mapping(source = "entryDate", target = "dateOfStartWork")
	@Mapping(source = "leader.id", target = "leader.employeeId")
	@Mapping(source = "leader.title", target = "leader.position.name")
	@Mapping(source = "leader.entryDate", target = "leader.dateOfStartWork")
	@Mapping(target = "company.companyType", ignore = true)
//	@Mapping(target = "leader.company", ignore = true)
	@Mapping(target = "leader.company.companyType", ignore = true)
	Employee dtoToEmployee(EmployeeDto employeeDto);

	@InheritInverseConfiguration
	EmployeeDto employeeToDto(Employee employee);
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	List<Employee> dtosToEmployees(List<EmployeeDto> employees);

}
