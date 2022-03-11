package hu.webuni.hr.dodi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.dodi.service.DefaultEmployeeService;
import hu.webuni.hr.dodi.service.EmployeeService;
import hu.webuni.hr.dodi.service.SmartEmployeeService;

@Configuration
@Profile("!smart")
public class EmployeeConfiguration {

	@Bean
	EmployeeService employeeService( ) {
		return new DefaultEmployeeService();
	}
}
