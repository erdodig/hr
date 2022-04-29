package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.Employee;
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
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	PositionRepository positionRepository;
	
//	@BeforeEach
//	public void init() {
//		positionRepository.deleteAll();
//		employeeRepository.deleteAll();
//		holidayRequestService.deleteAll();
//	}
	
//	@Test
//	void testCreateHolidayRequest() throws Exception {
//		
//		HolidayRequestDto newHolidayRequestDto = createHolidayRequestDto(false);
//		
//		newHolidayRequestDto = saveHolidayRequest(newHolidayRequestDto);
//		
//		assertThat(newHolidayRequestDto.getId()).isGreaterThan(0L);
//		
//	}

	@Test
	void testAllowedHolidayRequest() {

		HolidayRequestDto updateHolidayRequestDto = createHolidayRequestDto(true);
		
		updateHolidayRequestDto = allowedHolidayRequest(updateHolidayRequestDto.getId());
		
		assertThat(updateHolidayRequestDto.getHolidayRequestState()).isEqualTo(HolidayRequestState.ALLOWED);
	}
	
	private HolidayRequestDto allowedHolidayRequest(Long id) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/" + id + "?allowed=true")
//				.bodyValue()
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(HolidayRequestDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayRequestDto createHolidayRequestDto(boolean saved) {
		
		Position position = new Position("fejlesztő", Qualification.UNIVERSITY);
		position = positionRepository.save(position);
		
		Employee employee = new Employee(null, "Kiss Kázmér", 250000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		
		HolidayRequestDto holidayRequestDto = new HolidayRequestDto(LocalDate.of(2022, 6, 10), LocalDate.of(2022, 6, 20), 
				LocalDateTime.now(), employeeMapper.employeeToDto(employee));
		
		if (saved)
			holidayRequestDto = saveHolidayRequest(holidayRequestDto);

		return holidayRequestDto;
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
