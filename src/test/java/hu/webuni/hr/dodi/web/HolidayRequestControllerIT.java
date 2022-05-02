package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.mapper.HolidayRequestMapper;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestByExample;
import hu.webuni.hr.dodi.model.HolidayRequestState;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.model.Qualification;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;
import hu.webuni.hr.dodi.service.HolidayRequestService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HolidayRequestControllerIT {

	private static final String BASE_URI = "/api/holidays";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	HolidayRequestService holidayRequestService;
	
	@Autowired
	HolidayRequestMapper holidayRequestMapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	PositionRepository positionRepository;
	
	@BeforeEach
	public void init() {
		holidayRequestService.deleteAll();
	}
	
	@Test
	void testCreateHolidayRequest() throws Exception {
		
		HolidayRequestDto newHolidayRequestDto = createHolidayRequestDto();
		
		assertThat(newHolidayRequestDto.getId()).isGreaterThan(0L);
		
	}

	@Test
	void testAllowedHolidayRequest() {

		HolidayRequestDto updateHolidayRequestDto = createHolidayRequestDto();
		
		Employee employee = createEmployee("fejlesztő", Qualification.HIGH_SCHOOL, 
				"Nagy Ádám", 280000, LocalDateTime.of(2020, 07, 01, 8, 0, 0));
		
		updateHolidayRequestDto.setLeader(employeeMapper.employeeToDto(employee));
		
		updateHolidayRequestDto = allowedHolidayRequest(updateHolidayRequestDto.getId());
		
		assertThat(updateHolidayRequestDto.getHolidayRequestState()).isEqualTo(HolidayRequestState.ALLOWED);
	}

	@Test
	void testDeleteHolidayRequest() {

		HolidayRequestDto holidayRequestDto = createHolidayRequestDto();
		
		holidayRequestDto = allowedHolidayRequest(holidayRequestDto.getId());
		
		deleteHolidayRequest(holidayRequestDto.getId());
		
		HolidayRequestDto afterHolidayRequestDto = holidayRequestMapper.holidayRequestToDto(holidayRequestService
				.findById(holidayRequestDto.getId()));
		
		assertThat(afterHolidayRequestDto.getHolidayRequestState()).isNotEqualTo(HolidayRequestState.REQUEST);
	}
	
	@Test
	void testModifyHolidayRequest() throws Exception {
		
		HolidayRequestDto holidayRequestDto = createHolidayRequestDto();
		
		holidayRequestDto.setFromDate(LocalDate.of(2022, 6, 15));
		
		holidayRequestDto = saveHolidayRequest(holidayRequestDto);
		
		assertThat(holidayRequestDto.getFromDate()).isEqualTo(LocalDate.of(2022, 6, 15));		
	}
	
	@Test
	void testFindHolidayRequestByState() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setHolidayRequestState(HolidayRequestState.ALLOWED);
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);		
		assertThat(holidayRequestDtos.get(0).getHolidayRequestState()).isEqualTo(HolidayRequestState.ALLOWED);		
	}
	
	@Test
	void testFindHolidayRequestByEmployeeOrLeader() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setRequestName("kis");
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(2);	
		
		example = new HolidayRequestByExample();
		example.setLeaderName("nagy");
		
		holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);	
	}
	
	@Test
	void testFindHolidayRequestByRequestTime() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setFromRequestTime(LocalDateTime.of(2022, 3, 10, 0, 0, 0));
		example.setToRequestTime(LocalDateTime.of(2022, 3, 16, 0, 0, 0));
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(2);	
		
		example = new HolidayRequestByExample();
		example.setFromRequestTime(LocalDateTime.of(2022, 3, 16, 0, 0, 0));
		example.setToRequestTime(LocalDateTime.of(2022, 4, 2, 0, 0, 0));
		
		holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);	
	}
	
	@Test
	void testFindHolidayRequestByHolidayDates() throws Exception {
		
		List<HolidayRequest> holidayRequests = createHolidayRequests();
		
		createTestState(holidayRequests);
		
		HolidayRequestByExample example = new HolidayRequestByExample();
		example.setFromDate(LocalDate.of(2022, 3, 5));
		example.setToDate(LocalDate.of(2022, 3, 20));
		
		List<HolidayRequestDto> holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(2);	
		
		example = new HolidayRequestByExample();
		example.setFromDate(LocalDate.of(2022, 3, 30));
		example.setToDate(LocalDate.of(2022, 4, 2));
		
		holidayRequestDtos = findAllHolidayRequestByExample(example, 0, 5);
		
		assertThat(holidayRequestDtos.size()).isEqualTo(1);	
	}
	
	private void createTestState(List<HolidayRequest> holidayRequests) {
		
		HolidayRequest holidayRequest = holidayRequests.get(1);
			
		Employee employee = createEmployee("fejlesztő", Qualification.HIGH_SCHOOL, 
				"Nagy Főnök", 580000, LocalDateTime.of(2015, 01, 01, 8, 0, 0));
		
		holidayRequest.setLeader(employee);
		holidayRequest.setHolidayRequestState(HolidayRequestState.ALLOWED);
		
		holidayRequestService.update(holidayRequest);		
	}

	private List<HolidayRequest> createHolidayRequests() {
		
		List<HolidayRequest> holidayRequests = new ArrayList<>();
		
		Employee employee1 = createEmployee("fejlesztő", Qualification.UNIVERSITY, 
				"Kiss Kázmér", 250000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
		Employee employee2 = createEmployee("fejlesztő", Qualification.COLLEGE, 
				"Kiss Ádám", 280000, LocalDateTime.of(2019, 05, 01, 8, 0, 0));
		
		Employee employee3 = createEmployee("fejlesztő", Qualification.HIGH_SCHOOL, 
				"Nagy Ádám", 280000, LocalDateTime.of(2020, 07, 01, 8, 0, 0));
		
		HolidayRequest holidayRequest = new HolidayRequest(LocalDate.of(2022, 3, 10), 
				LocalDate.of(2022, 3, 20), LocalDateTime.of(2022, 3, 10, 8, 0, 0), employee1);
		holidayRequest = holidayRequestService.save(holidayRequest);
		holidayRequests.add(holidayRequest);
		
		holidayRequest = new HolidayRequest(LocalDate.of(2022, 3, 15), 
				LocalDate.of(2022, 3, 25), LocalDateTime.of(2022, 3, 15, 8, 0, 0), employee2);
		holidayRequest = holidayRequestService.save(holidayRequest);
		holidayRequests.add(holidayRequest);
		
		holidayRequest = new HolidayRequest(LocalDate.of(2022, 4, 1), 
				LocalDate.of(2022, 4, 10), LocalDateTime.of(2022, 4, 1, 8, 0, 0), employee3);
		holidayRequest = holidayRequestService.save(holidayRequest);
		holidayRequests.add(holidayRequest);
		
		return holidayRequests;
		
	}

	private List<HolidayRequestDto> findAllHolidayRequestByExample(HolidayRequestByExample holidayRequestByExample, 
			int page, int count) {

		return webTestClient
				.post()
				.uri(BASE_URI + "/byExample/" + String.valueOf(page) + "/" + String.valueOf(count))
				.bodyValue(holidayRequestByExample)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();		
	}
	
	private ResponseSpec deleteHolidayRequest(long id) {
		
		return webTestClient
				.post()
				.uri(BASE_URI + "/" + String.valueOf(id))
				.exchange();
	}
	
	private HolidayRequestDto allowedHolidayRequest(Long id) {
		
		return webTestClient
				.put()
				.uri(BASE_URI + "/" + id + "?allowed=true")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayRequestDto createHolidayRequestDto() {
		
		Employee employee = createEmployee("fejlesztő", Qualification.UNIVERSITY, 
				"Kiss Kázmér", 250000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		
		HolidayRequestDto holidayRequestDto = new HolidayRequestDto(LocalDate.of(2022, 6, 10), LocalDate.of(2022, 6, 20), 
				LocalDateTime.now(), employeeMapper.employeeToDto(employee));
		
		holidayRequestDto = saveHolidayRequest(holidayRequestDto);

		return holidayRequestDto;
	}
	
	private Employee createEmployee(String positionName, Qualification qualification, String employeeName, 
			int salary, LocalDateTime dateOfStartWork) {
		
		Position position = new Position(positionName, qualification);
		position = positionRepository.save(position);
		
		Employee employee = new Employee(null, employeeName, salary, dateOfStartWork);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		
		return employee;
	}

	private HolidayRequestDto saveHolidayRequest(HolidayRequestDto newHolidayRequestDto) {
		
		return webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(newHolidayRequestDto)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}

}
