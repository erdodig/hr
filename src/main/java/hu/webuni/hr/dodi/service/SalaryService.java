package hu.webuni.hr.dodi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;

@Service
public class SalaryService {
	
	@Autowired
	private EmployeeService employeeService;

	public int getPayRaisePercent(Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}

}
