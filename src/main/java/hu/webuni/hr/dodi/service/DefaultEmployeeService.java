package hu.webuni.hr.dodi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.model.Employee;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService {
	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return config.getSalary().getDef().getPercent();
	}

}
