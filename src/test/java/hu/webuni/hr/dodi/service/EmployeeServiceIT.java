package hu.webuni.hr.dodi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.web.EmployeeController;

@SpringBootTest
public class EmployeeServiceIT {

	@Autowired
	EmployeeController employeeController;
	
	@Test
	void testFalseCreateEmployee() {
	
		EmployeeDto employeeDto = new EmployeeDto(1L, "", "", -1, LocalDateTime.of(2010, 6, 1, 8, 0));		
		assertThat(employeeDto).isEqualTo((employeeController.createEmployee(employeeDto)));
	}
	
	@Test
	void testTrueCreateEmployee() {
		
		EmployeeDto employeeDto = new EmployeeDto(1L, "Kiss János", "worker", 280000, LocalDateTime.of(2010, 6, 1, 8, 0));		
		assertThat(employeeDto).isEqualTo((employeeController.createEmployee(employeeDto)));				
	}
	
	@Test
	void testFalseModifyEmployee() {
	
		EmployeeDto employeeDto = new EmployeeDto(1L, "", "", -1, LocalDateTime.of(2010, 6, 1, 8, 0));		
		assertThat(employeeDto).isEqualTo((employeeController.modifyEmployee(1, employeeDto)));
	}
	
	@Test
	void testTrueModifyEmployee() {
	
		EmployeeDto employeeDto = new EmployeeDto(1L, "Kiss János", "worker", 280000, LocalDateTime.of(2010, 6, 1, 8, 0));		
		assertThat(employeeDto).isEqualTo((employeeController.modifyEmployee(1, employeeDto)));
	}
	
}
