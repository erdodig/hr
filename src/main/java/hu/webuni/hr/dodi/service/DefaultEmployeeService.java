package hu.webuni.hr.dodi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.model.Employee;

@Service
public class DefaultEmployeeService implements EmployeeService {
	
//	@Value("${hr.employee.default.percent}")
//	private int defaultPercent;

	@Autowired
	HrConfigProperties config;
	
	@Override
	public int getPayRaisePercent(Employee employee) {
		
//		return defaultPercent;
		
		return config.getEmployee().getDef().getPercent();
	}

}
