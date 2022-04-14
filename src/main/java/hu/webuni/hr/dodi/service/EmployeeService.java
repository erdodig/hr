package hu.webuni.hr.dodi.service;

import java.util.List;
import java.util.Optional;

import hu.webuni.hr.dodi.model.Employee;

public interface EmployeeService {
	
	public Employee save(Employee employee);
	
	public Employee update(Employee employee);
	
	public List<Employee> findAll();

	public Optional<Employee> findByid(Long id);
	
	public void delete(long id);

	int getPayRaisePercent(Employee employee);
}
