package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.config.HrConfigProperties.Smart;
import hu.webuni.hr.dodi.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {
	
	@Autowired
	HrConfigProperties config;
	
	@Override
	public int getPayRaisePercent(Employee employee) {
		
		double years = (LocalDateTime.now().getYear() - employee.getStartOfWork().getYear())
						+ (employee.getStartOfWork().getMonthValue() / 12.0);
		
		for (Smart smart : config.getEmployee().getSmarts()) {
		
			if (years >= smart.getLimit()) {
			
				return smart.getPercent();
			}
		}
		
		return 0;
	}

}
