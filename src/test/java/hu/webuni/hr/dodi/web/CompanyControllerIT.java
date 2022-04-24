package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
import hu.webuni.hr.dodi.repository.CompanyRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyControllerIT {

	private static final String BASE_URI = "/api/companies";

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@BeforeEach
	public void init() {
		companyRepository.deleteAll();
	}
	
	@Test
	void testAddNewEmployee() throws Exception {
	
		List<CompanyDto> beforeCompanyDtos = getAllCompanies();
		
		EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "fejlesztő", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
		addEmployee(beforeCompanyDtos.get(0).getId(), newEmployee).expectStatus().isOk();
		
		List<CompanyDto> afterCompanyDtos = getAllCompanies();
		
		assertThat(afterCompanyDtos.size()).isEqualTo(beforeCompanyDtos.size() + 1);
	}
	
	@Test
	void testDeleteEmployee() throws Exception {
	
		CompanyDto beforeCompanyDto = getAllCompanies().get(0);
		
		List<EmployeeDto> beforeEmployees = beforeCompanyDto.getEmployees();
		
		deleteEmployee(beforeCompanyDto.getId(), beforeEmployees.get(0).getId()).expectStatus().isOk();
		
		CompanyDto afterCompanyDto = getAllCompanies().get(0);
		
		List<EmployeeDto> afterEmployees = afterCompanyDto.getEmployees();
		
		assertThat(afterEmployees.size()).isEqualTo(beforeEmployees.size() - 1);
	}
	
	private ResponseSpec deleteEmployee(long companyId, long employeeId) {
		
		return webTestClient
				.post()
				.uri(BASE_URI + "/" + String.valueOf(companyId) + "/employees/" + String.valueOf(employeeId))
				.exchange();
	}
	
	private ResponseSpec addEmployee(long companyId, EmployeeDto newEmployee) {
		
		return webTestClient
				.post()
				.uri(BASE_URI + "/" + String.valueOf(companyId) + "/employees")
				.bodyValue(newEmployee)
				.exchange();
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

	private CompanyDto replaceEmployees(Long companyId, List<EmployeeDto> employees) {
		
		CompanyDto responseList = webTestClient
				.get()
				.uri(BASE_URI + "/" + companyId + "/employees")
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
