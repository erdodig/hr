package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;
import hu.webuni.hr.dodi.service.EmployeeService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

	private static final String BASE_URI = "/api/employees";

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
	}
	
	@Test
	void testThatNewValidEmployeeCanBeSaved() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
		saveEmployee(newEmployee)
		.expectStatus()
		.isOk();

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.get(employeesAfter.size()-1))
			.usingRecursiveComparison()
			.ignoringFields("id")
			.isEqualTo(newEmployee);
	}
	
	@Test
	void testThatNewInvalidEmployeeCannotBeSaved() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newEmployee = newInvalidEmployee();
		saveEmployee(newEmployee)
		.expectStatus()
		.isBadRequest();

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
	}

	private EmployeeDto newInvalidEmployee() {
		return new EmployeeDto(0L, "", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
	}
	
	@Test
	void testThatEmployeeCanBeUpdatedWithValidFields() throws Exception {

		EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
		EmployeeDto savedEmployee = saveEmployee(newEmployee)
				.expectStatus().isOk()
				.expectBody(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		
		List<EmployeeDto> employeesBefore = getAllEmployees();
		savedEmployee.setName("modified");
		modifyEmployee(savedEmployee)
		.expectStatus()
		.isOk();

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size()-1))
			.usingRecursiveComparison()
			.isEqualTo(savedEmployee);
	}
	
	@Test
	void testThatEmployeeCannotBeUpdatedWithInvalidFields() throws Exception {
		EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		EmployeeDto savedEmployee = saveEmployee(newEmployee)
				.expectStatus().isOk()
				.expectBody(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto invalidEmployee = newInvalidEmployee();
		invalidEmployee.setId(savedEmployee.getId());
		modifyEmployee(invalidEmployee).expectStatus().isBadRequest();

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size()-1))
			.usingRecursiveComparison()
			.isEqualTo(savedEmployee);
	}
	
	@Test
	void testFindEmployeesByExample() throws Exception {

		Employee employee = new Employee(0L, "Kiss Márton", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		employee.setCompany(companyRepository.findAll().get(0));
		employee.setPosition(positionRepository.findAll().get(0));
		
		List<EmployeeDto> employees = findEmployeesByExample(employee);
		
		assertThat(employees.stream().map(EmployeeDto::getId).collect(Collectors.toList())).containsExactly(employee.getEmployeeId());
	
	}
	
	private List<EmployeeDto> findEmployeesByExample(Employee employee) {
		List<EmployeeDto> responseList = webTestClient
				.get()
				.uri(BASE_URI + "/findEmployees")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		return responseList;
	}

	private ResponseSpec modifyEmployee(EmployeeDto newEmployee) {
		String path = BASE_URI + "/" + newEmployee.getId();
		return webTestClient
				.put()
				.uri(path)
				.bodyValue(newEmployee)
				.exchange();
	}
	
	private ResponseSpec saveEmployee(EmployeeDto newEmployee) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(newEmployee)
				.exchange();
	}

	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> responseList = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
		Collections.sort(responseList, Comparator.comparing(EmployeeDto::getId));
		return responseList;
	}

}
