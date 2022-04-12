package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import hu.webuni.hr.dodi.model.Employee;

public abstract class EmployeeService {
	
	private Map<Long, Employee> employees = new HashMap<>();
	{
		employees.put(1L, new Employee(1L, "Kiss János", "worker", 250000, LocalDateTime.of(2010, 10, 22, 8, 0)));
		employees.put(2L, new Employee(2L, "Meta Flóra", "secretary", 320000, LocalDateTime.of(2020, 6, 1, 8, 0)));
	}

	public Employee save(Employee employee) {
		employees.put(employee.getId(), employee);
		return employee;
	}
	
	public List<Employee> findAll() {
		return new ArrayList<>(employees.values());
	}
	
	public Employee findByid(long id) {
		return employees.get(id);
	}
	
	public void delete(long id) {
		employees.remove(id);
	}

	public int getPayRaisePercent(@Valid Employee employee) {
		return 0;
	}
}
