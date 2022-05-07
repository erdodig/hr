package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.model.Qualification;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

	private static final String BASE_URI = "/api/employees";
	
	private static Position position;
	
	private Company company2;

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
//		initDB();
	}
	
	private void initDB() {
		
		position = new Position("fejlesztő", Qualification.UNIVERSITY);
		position = positionRepository.save(position);
		
		List<Employee> employees = new ArrayList<>();
		
		Employee employee1 = new Employee(null, "Kiss Kázmér", 350000, LocalDateTime.of(2015, 10, 1, 8, 0, 0));
		employee1.setPosition(position);
		employeeRepository.save(employee1);
		employees.add(employee1);
		
		Employee employee2 = new Employee(null, "Nagy Lajos", 286000, LocalDateTime.of(2015, 10, 1, 8, 0, 0));
		employee2.setPosition(position);
		employeeRepository.save(employee2);
		employees.add(employee2);
		
		Company company1 = new Company(null, 123, "Kovács Kft.", "1010. Budapest, Harangvirág utca 12.", employees); 
		companyRepository.save(company1);	
		
		for (Employee employee3 : company1.getEmployees()) {
			employee3.setCompany(company1);
			employeeRepository.save(employee3);
		}
		
		employees.clear();
		
		Employee employee4 = new Employee(null, "Meta Flóra", 320000, LocalDateTime.of(2020, 4, 1, 8, 0, 0));
		employee4.setPosition(position);
		employeeRepository.save(employee4);
		employees.add(employee4);
		
		Employee employee5 = new Employee(null, "Minden Áron", 290000, LocalDateTime.of(2021, 5, 20, 8, 0, 0));
		employee5.setPosition(position);
		employeeRepository.save(employee5);
		employees.add(employee5);

		Company company2 = new Company(null, 456, "Fa faragó Kft.", "7630. Pécs, Basamalom út 84.", employees); 
		companyRepository.save(company2);
		
		for (Employee employee6 : company2.getEmployees()) {
			employee6.setCompany(company2);
			employeeRepository.save(employee6);
		}
		
	}
	
	@Test
	void testThatNewValidEmployeeCanBeSaved() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "fejlesztő", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
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
	void testFindEmployeesById() throws Exception {
		
		initDB();

//		Employee employee1 = new Employee(null, "Kiss Kázmér", 350000, LocalDateTime.of(2015, 10, 1, 8, 0, 0));
		
		Employee exampleEmployee = new Employee();
		exampleEmployee.setName("Kiss Kázmér");
		
		List<Employee> realEmployees = employeeRepository.findByName("Kiss Kázmér");
		
		List<Employee> employees = employeeMapper.dtosToEmployees(findEmployeesByExample(exampleEmployee));
		
		assertThat(employees.size()).isEqualTo(1);
		assertThat(realEmployees.size()).isEqualTo(1);
		assertThat(employees.get(0)).isEqualTo(realEmployees.get(0));
	
	}
	
	@Test
	void testFindEmployeesByName() throws Exception {
		
		initDB();
		
		Employee exampleEmployee = new Employee();
		exampleEmployee.setName("M");
		
		List<Employee> employees = employeeMapper.dtosToEmployees(findEmployeesByExample(exampleEmployee));
		
		assertThat(employees.size()).isEqualTo(2);
	
	}
	
	@Test
	void testFindEmployeesBySalary() throws Exception {
		
		initDB();
		
		/**
		 * 300.000 between 285.000 and 315.000
		 */
		
		Employee exampleEmployee = new Employee();
		exampleEmployee.setSalary(300000);
		
		List<Employee> employees = employeeMapper.dtosToEmployees(findEmployeesByExample(exampleEmployee));
		
		assertThat(employees.size()).isEqualTo(2);
	
	}
	
	@Test
	void testFindEmployeesByDateOfStartWork() throws Exception {
		
		initDB();
		
		// 2015, 10, 1, 8, 0, 0
		
		Employee exampleEmployee = new Employee();
		exampleEmployee.setDateOfStartWork(LocalDateTime.of(2015, 10, 1, 8, 0, 0));
		
		List<Employee> employees = employeeMapper.dtosToEmployees(findEmployeesByExample(exampleEmployee));
		
		assertThat(employees.size()).isEqualTo(2);
	
	}
	
	private List<EmployeeDto> findEmployeesByExample(Employee employee) {
		List<EmployeeDto> responseList = webTestClient
				.post()
				.uri(BASE_URI + "/byExample")
				.bodyValue(employee)
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
