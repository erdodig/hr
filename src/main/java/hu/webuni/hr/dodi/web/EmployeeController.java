package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}
	
	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		
		Employee employee = employeeService.findByid(id).get();
		
		if (employee != null)
			return employeeMapper.employeeToDto(employee);
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		
		return employeeMapper.employeeToDto(employee);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		
		Employee employee = employeeService.update(employeeMapper.dtoToEmployee(employeeDto));
		
		if(employee == null) {
			
			return ResponseEntity.notFound().build();
			
		} else {
			
			return ResponseEntity.ok(employeeMapper.employeeToDto(employee));
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeService.delete(id);
	}
	
	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody @Valid Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}
	
	@PutMapping("/{jobtitle}")
	public ResponseEntity<List<EmployeeDto>> getByJobTitle(@PathVariable String jobTitle) {
		
		List<Employee> employees = employeeRepository.findByJobTitle(jobTitle);
		
		if(employees == null) {
			
			return ResponseEntity.notFound().build();
			
		} else {
			
			return ResponseEntity.ok(employeeMapper.employeesToDtos(employees));
		}
	}
	
	@PutMapping("/{jobtitle}")
	public ResponseEntity<List<EmployeeDto>> getByNameStartingWithIgnoreCase(@PathVariable String name) {
		
		List<Employee> employees = employeeRepository.findByNameStartingWithIgnoreCase(name);
		
		if(employees == null) {
			
			return ResponseEntity.notFound().build();
			
		} else {
			
			return ResponseEntity.ok(employeeMapper.employeesToDtos(employees));
		}
	}
	
	@PutMapping("/{jobtitle}")
	public ResponseEntity<List<EmployeeDto>> getByDateOfStartWorkBetween(@PathVariable LocalDateTime start, 
				@PathVariable LocalDateTime end) {
		
		List<Employee> employees = employeeRepository.findByDateOfStartWorkBetween(start, end);
		
		if(employees == null) {
			
			return ResponseEntity.notFound().build();
			
		} else {
			
			return ResponseEntity.ok(employeeMapper.employeesToDtos(employees));
		}
	}
	
}
