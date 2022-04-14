package hu.webuni.hr.dodi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.dodi.service.AbstractEmployeeService;
import hu.webuni.hr.dodi.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartSalaryConfiguration {

	@Bean
	public AbstractEmployeeService employeeService() {
		return new SmartEmployeeService();
	}
}
