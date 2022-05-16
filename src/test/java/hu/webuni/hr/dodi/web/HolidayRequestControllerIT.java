package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.time.LocalDate;
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

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.dto.LoginDto;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.mapper.HolidayRequestMapper;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestByExample;
import hu.webuni.hr.dodi.model.HolidayRequestState;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.HolidayRequestRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;
import hu.webuni.hr.dodi.service.HolidayRequestService;
import hu.webuni.hr.dodi.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HolidayRequestControllerIT {

	private static final String BEARER = "Bearer ";

	private static final String AUTHORIZATION = "Authorization";

	private static final String BASE_URI = "/api/holidays";

	private static final String LOGIN_URI = "/api/login";
	
	private String jwtToken;
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	HolidayRequestService holidayRequestService;
	
	@Autowired
	HolidayRequestRepository holidayRequestRepository;
	
	@Autowired
	HolidayRequestMapper holidayRequestMapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	InitDbService initDbService;
	
	@BeforeEach
	public void init() throws URISyntaxException {
		
		holidayRequestRepository.deleteAll();
		employeeRepository.deleteAll();
		
		initDbService.initDb();
	}
	
	@Test
	void testCreateHolidayRequest() throws URISyntaxException {
		
		HolidayRequestDto newHolidayRequestDto = createHolidayRequest("Kiss János");
		
		assertThat(newHolidayRequestDto.getId()).isGreaterThan(0L);		
	}
	
	@Test
	void testModifyHolidayRequest() throws Exception {

		HolidayRequestDto holidayRequestDto = createHolidayRequest("Kiss János");
		
		holidayRequestDto.setFromDate(LocalDate.of(2022, 6, 15));
		
		holidayRequestDto = saveHolidayRequest(holidayRequestDto, jwtToken);
		
		assertThat(holidayRequestDto.getFromDate()).isEqualTo(LocalDate.of(2022, 6, 15));		
	}

	@Test
	void testAllowedHolidayRequest() throws URISyntaxException {

		HolidayRequestDto holidayRequestDto = createHolidayRequest("Kiss János");
		
		EmployeeDto leaderDto = employeeMapper.employeeToDto(employeeRepository.findByName("Leader Péter").get(0)); 
		holidayRequestDto.setLeader(leaderDto);
		
		jwtToken = login("leader");
		
		holidayRequestDto = allowedHolidayRequest(holidayRequestDto, jwtToken);
		
		HolidayRequestDto afterHolidayRequestDto = holidayRequestMapper.holidayRequestToDto(holidayRequestService
				.findById(holidayRequestDto.getId()));
		
		assertThat(afterHolidayRequestDto.getHolidayRequestState()).isEqualTo(HolidayRequestState.ALLOWED);
	}

	@Test
	void testDeleteHolidayRequest() throws URISyntaxException {

		HolidayRequestDto holidayRequestDto = createHolidayRequest("Kiss János");
		
		deleteHolidayRequest(holidayRequestDto.getId()/*, holidayRequestDto*/, jwtToken);
		
		HolidayRequest afterHolidayRequest = holidayRequestService.findById(holidayRequestDto.getId());

		assertThat(afterHolidayRequest).isNull();
	}
	
	@Test
	void testFindHolidayRequestByState() throws Exception {
				
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setHolidayRequestState(HolidayRequestState.ALLOWED);
		
		jwtToken = login("kiss");
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5, jwtToken);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);		
		assertThat(holidayRequestDtos.get(0).getHolidayRequestState()).isEqualTo(HolidayRequestState.ALLOWED);		
	}
	
	@Test
	void testFindHolidayRequestByRequestTime() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example1 = new HolidayRequestByExample();
		example1.setFromRequestTime(LocalDateTime.of(2022, 3, 10, 0, 0, 0));
		example1.setToRequestTime(LocalDateTime.of(2022, 3, 16, 0, 0, 0));
		
		jwtToken = login("kiss");
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example1, 0, 5, jwtToken);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(2);	
		
		HolidayRequestByExample example2 = new HolidayRequestByExample();
		example2.setFromRequestTime(LocalDateTime.of(2022, 3, 16, 0, 0, 0));
		example2.setToRequestTime(LocalDateTime.of(2022, 4, 2, 0, 0, 0));
		
		holidayRequestDtos = findAllHolidayRequestByExample(example2, 0, 5, jwtToken);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);	
	}
	
	@Test
	void testFindHolidayRequestByHolidayDates() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setFromDate(LocalDate.of(2022, 7, 5));
		example.setToDate(LocalDate.of(2022, 7, 20));
		
		jwtToken = login("kiss");
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5, jwtToken);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(2);	
		
		example = new HolidayRequestByExample();
		example.setFromDate(LocalDate.of(2022, 7, 30));
		example.setToDate(LocalDate.of(2022, 8, 2));
		
		holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5, jwtToken);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);	
	}

	public String login(String username) throws URISyntaxException {
		
		LoginDto loginDto = new LoginDto(username, "pass");
		
		return loginRequest(loginDto);		
	}

	private String loginRequest(LoginDto loginDto) throws URISyntaxException {
		
		return webTestClient
				.post()
				.uri(LOGIN_URI)
				.bodyValue(loginDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayRequestDto createHolidayRequest(String employeeName) throws URISyntaxException {
		
		EmployeeDto employeeDto = employeeMapper.employeeToDto(employeeRepository.findByName(employeeName).get(0));
		
		jwtToken = login(employeeDto.getUsername());
		
		return createHolidayRequestDto(jwtToken, employeeDto);		
	}
	
	private void createTestState(List<HolidayRequest> holidayRequests) {
		
		HolidayRequest holidayRequest = holidayRequests.get(1);
			
		holidayRequest.setHolidayRequestState(HolidayRequestState.ALLOWED);
		
		holidayRequestService.update(holidayRequest);		
	}

	private List<HolidayRequest> createHolidayRequests() {
		
		List<HolidayRequest> holidayRequests = new ArrayList<>();
		
		Employee employee1 = employeeRepository.findByName("Kiss János").get(0);
		Employee employee2 = employeeRepository.findByName("Nagy János").get(0);
		Employee employee3 = employeeRepository.findByName("Leader Péter").get(0);
		
		HolidayRequest holidayRequest1 = new HolidayRequest(LocalDate.of(2022, 7, 10), 
				LocalDate.of(2022, 7, 20), LocalDateTime.of(2022, 3, 10, 8, 0, 0), employee1);
		holidayRequest1 = holidayRequestService.save(holidayRequest1);
		holidayRequests.add(holidayRequest1);
		
		HolidayRequest holidayRequest2 = new HolidayRequest(LocalDate.of(2022, 7, 15), 
				LocalDate.of(2022, 7, 25), LocalDateTime.of(2022, 3, 15, 8, 0, 0), employee2);
		holidayRequest2 = holidayRequestService.save(holidayRequest2);
		holidayRequests.add(holidayRequest2);
		
		HolidayRequest holidayRequest3 = new HolidayRequest(LocalDate.of(2022, 8, 1), 
				LocalDate.of(2022, 8, 10), LocalDateTime.of(2022, 4, 1, 8, 0, 0), employee3);
		holidayRequest3 = holidayRequestService.save(holidayRequest3);
		holidayRequests.add(holidayRequest3);
		
		return holidayRequests;
		
	}

	private List<HolidayRequestDto> findAllHolidayRequestByExample(HolidayRequestByExample holidayRequestByExample, 
			int page, int count, String jwtToken) {

		return webTestClient
				.post()
				.uri(BASE_URI + "/byExample/" + String.valueOf(page) + "/" + String.valueOf(count))
				.header(AUTHORIZATION, BEARER + jwtToken)
				.bodyValue(holidayRequestByExample)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();		
	}
	
	private void deleteHolidayRequest(long id, /*HolidayRequestDto holidayRequestDto,*/ String jwtToken) {
		
		webTestClient
				.delete()
				.uri(BASE_URI + "/" + String.valueOf(id))
				.header(AUTHORIZATION, BEARER + jwtToken)
				.exchange();
//				.expectStatus().isOk()
//				.expectBody()
//				.json("{\"id\": 17,\"fromDate\": \"2022-06-10\",\"toDate\": \"2022-06-20\",\"requestTime\": \"2022-05-16T09:26:41.858\",\"requestingEmployee\": {\"id\": 11,\"name\": \"Kiss János\",\"title\": \"fejlesztő\",\"salary\": 300000,\"entryDate\": \"2022-05-08T04:53:52.152235\",\"company\": null,\"username\": \"kiss\",\"password\": \"pass\"},\"leader\": {\"id\": 13,\"name\": \"Leader Péter\",\"title\": \"fejlesztő\",\"salary\": 200000,\"entryDate\": \"2022-05-09T07:12:54.075382\",\"company\": {\"id\": 14,\"registrationNumber\": 10,\"name\": \"Fa vágó Kft.\",\"address\": \"\",\"employees\": []},\"username\": \"leader\",\"password\": \"pass\",\"roles\": [\"admin\",\"user\"],\"leader\": null}}");
	}
	
	private HolidayRequestDto allowedHolidayRequest(HolidayRequestDto holidayRequestDto, String jwtToken) {
		
		return webTestClient
				.put()
				.uri(BASE_URI + "/" + holidayRequestDto.getId() + "?allowed=true")
				.header(AUTHORIZATION, BEARER + jwtToken)
				.bodyValue(holidayRequestDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayRequestDto createHolidayRequestDto(String jwtToken, EmployeeDto employeeDto) {
		
		HolidayRequestDto holidayRequestDto = new HolidayRequestDto(LocalDate.of(2022, 6, 10), LocalDate.of(2022, 6, 20), 
				LocalDateTime.now(), employeeDto);
		
		holidayRequestDto = saveHolidayRequest(holidayRequestDto, jwtToken);

		return holidayRequestDto;
	}

	private HolidayRequestDto saveHolidayRequest(HolidayRequestDto newHolidayRequestDto, String jwtToken) {
		
		return webTestClient
				.post()
				.uri(BASE_URI)
				.header(AUTHORIZATION, BEARER + jwtToken)
				.bodyValue(newHolidayRequestDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}

}
