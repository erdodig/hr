package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import hu.webuni.hr.dodi.dto.CompanyDto;
import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.mapper.CompanyMapper;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.model.Qualification;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyControllerIT {

	private static final String BASE_URI = "/api/companies";
	
	private static Position position;

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	PositionRepository positionRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		initDB();
	}
	
	private void initDB() {
		
		position = new Position("fejlesztő", Qualification.UNIVERSITY);
		position = positionRepository.save(position);
		
		List<Employee> employees = new ArrayList<>();
		
		Employee employee1 = new Employee(null, "Kiss Kázmér", 350000, LocalDateTime.of(2015, 10, 1, 8, 0, 0));
		employee1.setPosition(position);
		employeeRepository.save(employee1);
		employees.add(employee1);
		
		Employee employee2 = new Employee(null, "Nagy Lajos", 280000, LocalDateTime.of(2017, 6, 10, 8, 0, 0));
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
	void testAddNewEmployee() throws Exception {
	
		List<CompanyDto> beforeCompanyDtos = getAllCompanies();
				
		Employee newEmployee = new Employee(null, "ABC", 200000, LocalDateTime.of(2020, 4, 1, 8, 0, 0));
		newEmployee.setPosition(position);
		
		addEmployee(beforeCompanyDtos.get(0).getId(), employeeMapper.employeeToDto(newEmployee)).expectStatus().isOk();
		
		List<CompanyDto> afterCompanyDtos = getAllCompanies();
		
		assertThat(afterCompanyDtos.get(0).getEmployees().size())
				.isEqualTo(beforeCompanyDtos.get(0).getEmployees().size() + 1);
	}
	
	@Test
	void testReplaceEmployees() throws Exception {
	
		CompanyDto sourceCompanyDto = getAllCompanies().get(0);
		CompanyDto destCompanyDto = getAllCompanies().get(1);
		
		destCompanyDto = replaceEmployees(destCompanyDto.getId(), sourceCompanyDto.getEmployees());
		
		boolean isOk = true;
		for (EmployeeDto employeeDto : sourceCompanyDto.getEmployees()) {
		
			if (!destCompanyDto.getEmployees().contains(employeeDto)) {
				isOk = false;
				break;
			}
		}
		
		assertThat(isOk).isEqualTo(true);
	}
	
	@Test
	void testDeleteEmployee() throws Exception {
	
		List<CompanyDto> beforeCompanyDtos = getAllCompanies();		
		List<EmployeeDto> beforeEmployees = beforeCompanyDtos.get(0).getEmployees();
		
		CompanyDto afterCompanyDto = deleteEmployee(beforeCompanyDtos.get(0).getId(), beforeEmployees.get(0).getId());
		
		List<EmployeeDto> afterEmployees = afterCompanyDto.getEmployees();
		
		assertThat(afterEmployees.size()).isEqualTo(beforeEmployees.size() - 1);
	}
	
	private CompanyDto deleteEmployee(long companyId, long employeeId) {
		
		CompanyDto responseList = webTestClient
				.delete()
				.uri(BASE_URI + "/" + String.valueOf(companyId) + "/employees/" + String.valueOf(employeeId))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(CompanyDto.class)
				.returnResult()
				.getResponseBody();
		
		return responseList;
	}
	
	private ResponseSpec addEmployee(long companyId, EmployeeDto newEmployee) {
		
		return webTestClient
				.post()
				.uri(BASE_URI + "/" + String.valueOf(companyId) + "/employees")
				.bodyValue(newEmployee)
				.exchange();
	}

	private CompanyDto replaceEmployees(Long companyId, List<EmployeeDto> employees) {
		
		CompanyDto responseList = webTestClient
				.put()
				.uri(BASE_URI + "/" + companyId + "/employees")
				.bodyValue(employees)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(CompanyDto.class)
				.returnResult()
				.getResponseBody();

		return responseList;
	}

	private List<CompanyDto> getAllCompanies() {
		
		List<CompanyDto> responseList = webTestClient
				.get()
				.uri(BASE_URI + "?full=true")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(CompanyDto.class)
				.returnResult()
				.getResponseBody();

		return responseList;
	}

}
