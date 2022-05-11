package hu.webuni.hr.dodi.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.dto.HolidayRequestDto;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.service.HolidayRequestService;
import hu.webuni.hr.dodi.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UserDetailsAndHolidayRequestIT {

	private static final String BASE_URI = "/api/holidays";
	
	@Autowired
	WebTestClient webTestClient;

    @Autowired
    private TestRestTemplate template;
	
	@Autowired
	HolidayRequestService holidayRequestService;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	InitDbService initDbService;
	
	@BeforeEach
	public void init() {
		holidayRequestService.deleteAll();
		employeeRepository.deleteAll();
		initDbService.initDb();
	}
	
	@Test
	void testCreateHolidayRequest() throws Exception {
		
		EmployeeDto employeeDto1 = employeeMapper.employeeToDto(employeeRepository.findByName("Kiss János").get(0));
		
		HolidayRequestDto holidayRequestDto = new HolidayRequestDto(LocalDate.of(2022, 5, 15), LocalDate.of(2022, 5, 25), 
				LocalDateTime.of(2022, 5, 10, 8, 0, 0), employeeDto1);
		
		holidayRequestDto = saveHolidayRequest(holidayRequestDto);
		
		assertThat(holidayRequestDto.getId()).isGreaterThan(0L);
		
	}

	private HolidayRequestDto saveHolidayRequest(HolidayRequestDto newHolidayRequestDto) throws URISyntaxException {
		
		return template
				.withBasicAuth("kiss", "pass")
				.postForObject(new URI(BASE_URI), newHolidayRequestDto, HolidayRequestDto.class);
	}

}
