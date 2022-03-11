package hu.webuni.hr.dodi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;

@Service
public class DefaultEmployeeService implements EmployeeService {
	
	@Value("${hr.employee.default.percent}")
	private int defaultPercent;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return defaultPercent;
	}

}
