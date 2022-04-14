package hu.webuni.hr.dodi.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;

@Service
public class SalaryService {

	private AbstractEmployeeService employeeService;

	public SalaryService(AbstractEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setNewSalary(Employee employee) {
		int newSalary = employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100;
		employee.setSalary(newSalary);
	}

}
