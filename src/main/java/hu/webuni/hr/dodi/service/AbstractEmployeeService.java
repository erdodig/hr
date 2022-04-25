package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.EmployeeRepository;

@Service
public abstract class AbstractEmployeeService implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		if(!employeeRepository.existsById(employee.getEmployeeId()))
			return null;
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public List<Employee> findEmployeesByExample(Employee employee) {

		Long id = employee.getEmployeeId();
		String employeeName = employee.getName();
		String positionName = employee.getPosition().getName();
		int salary = employee.getSalary();
		LocalDateTime dateOfStartWork = employee.getDateOfStartWork();
		String companyName = employee.getCompany().getName();
		
		Specification<Employee> spec = Specification.where(null);
		
		if (id > 0)
			spec = spec.and(EmployeeSpecifications.hasId(id));
		
		if (StringUtils.hasText(employeeName))
			spec = spec.and(EmployeeSpecifications.hasEmployeeName(employeeName));
		
		if (StringUtils.hasText(positionName))
			spec = spec.and(EmployeeSpecifications.hasPositionName(positionName));
		
		if (dateOfStartWork != null)
			spec = spec.and(EmployeeSpecifications.hasEntryDate(dateOfStartWork));
		
		if (StringUtils.hasText(companyName))
			spec = spec.and(EmployeeSpecifications.hasCompanyName(companyName));
		
		if (salary > 0)
			spec = spec.and(EmployeeSpecifications.hasSalary(salary));
			
		return employeeRepository.findAll(spec, Sort.by("id"));
	}
	
}
