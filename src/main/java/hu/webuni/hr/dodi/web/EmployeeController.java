package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.config.HrConfigProperties.Smart;
import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	@Autowired
	HrConfigProperties config;
	
	private Map<Long, EmployeeDto> employees = new HashMap<>();
	
	{
		employees.put(1L, new EmployeeDto(1L, "Meta Flóra", "Secretary", 350000, LocalDateTime.now()));
		employees.put(2L, new EmployeeDto(2L, "Para Zita", "Secretary", 380000, LocalDateTime.now()));
	}

	@GetMapping
	public List<EmployeeDto> getAll(@RequestParam(required = false) Integer minSalary) {
		
		if (minSalary == null)
			
			return new ArrayList<>(employees.values());
		
		else 
			
			return employees.values().stream().filter(e -> e.getSalary() > minSalary).collect(Collectors.toList());
			
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		
		EmployeeDto employeeDto = employees.get(id);
		
		if (employeeDto == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(employeeDto);
	}
	
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
		employeeDto.setStartOfWork(LocalDateTime.now());
		employees.put(employeeDto.getId(), employeeDto);
		return employeeDto;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		
		if (!employees.containsKey(id))
			return ResponseEntity.notFound().build();
		
		employeeDto.setStartOfWork(LocalDateTime.now());
		employeeDto.setId(id);
		employees.put(id, employeeDto);
		
		return ResponseEntity.ok(employeeDto);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employees.remove(id);
	}
	
	@PostMapping("/salary")
	public int getPayRaisePercent(@RequestParam(required = false) Boolean smart, @RequestBody Employee employee) {
	
		if (smart == null || smart == false) {
			
			return config.getEmployee().getDef().getPercent();
			
		} else {
		
			double years = (LocalDateTime.now().getYear() - employee.getStartOfWork().getYear())
							+ (employee.getStartOfWork().getMonthValue() / 12.0);
			
			for (Smart salarySmart : config.getEmployee().getSmarts()) {
			
				if (years >= salarySmart.getLimit()) {
				
					return salarySmart.getPercent();
				}
			}
			
			return 0;
		}
	}
	
}
