package hu.webuni.hr.dodi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	List<EmployeeDto> employeesToDtos(List<Employee> employess);

	EmployeeDto employeeToDto(Employee employee);

	Employee dtoToEmployee(EmployeeDto employeeDto);

	List<Employee> dtosToEmployees(List<EmployeeDto> employees);
}
