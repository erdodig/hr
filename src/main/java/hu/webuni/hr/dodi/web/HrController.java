package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.dodi.dto.EmployeeDto;

@RestController
@RequestMapping("/api/employees")
public class HrController {
	
	private Map<Long, EmployeeDto> employees = new HashMap<>();
	
	{
		employees.put(1L, new EmployeeDto(1L, "Meta Flóra", "Secretary", 350000, LocalDateTime.now()));
		employees.put(2L, new EmployeeDto(2L, "Para Zita", "Secretary", 350000, LocalDateTime.now()));
	}

	@GetMapping
	public List<EmployeeDto> getAll() {
		return new ArrayList<>(employees.values());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		
		EmployeeDto employeeDto = employees.get(id);
		
		if (employeeDto == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(employeeDto);
	}

	@GetMapping("/salary/{salary}")
	public List<EmployeeDto> getAllHigherSalary(@PathVariable int salary) {
		
		Map<Long, EmployeeDto> higherSalary = new HashMap<>();
		
		for (Map.Entry<Long, EmployeeDto> entry : employees.entrySet()) {
		
			if (entry.getValue().getSalary() > salary)
				higherSalary.put(entry.getKey(), entry.getValue());
		}
		
		return new ArrayList<>(higherSalary.values());
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
	
}
