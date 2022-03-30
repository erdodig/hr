package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.dodi.dto.CompanyDto;
import hu.webuni.hr.dodi.dto.EmployeeDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	private Map<Long, CompanyDto> companies = new HashMap<>();
	
	{
		companies.put(1L, new CompanyDto(1L, "134876", "Big Co.", "Budapest, Nagy u. 12."));
		companies.put(2L, new CompanyDto(2L, "987519", "Small Co.", "Pécs, Kiss u. 21."));
	}

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {	
		
		if (full == null || full == false) {
			
			companies.get(1L).setEmployeeDtos(new ArrayList<>());
			
			return new ArrayList<>(companies.values());
		
		} else {
			
			if (companies.get(1L).getEmployeeDtos().isEmpty()) {
				
				companies.get(1L).getEmployeeDtos().addAll(
						new ArrayList<>(
								List.of(
										new EmployeeDto(1L, "Meta Flóra", "Secretary", 350000, LocalDateTime.now()),
										new EmployeeDto(2L, "Para Zita", "Secretary", 380000, LocalDateTime.now())
										))
						);
			}

			return new ArrayList<>(companies.values());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getById(@PathVariable long id) {
		
		CompanyDto companyDto = companies.get(id);
		
		if (companyDto == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(companyDto);
	}
	
	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
		companies.put(companyDto.getId(), companyDto);
		return companyDto;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		
		if (!companies.containsKey(id))
			return ResponseEntity.notFound().build();
		
		companyDto.setId(id);
		companies.put(id, companyDto);
		
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companies.remove(id);
	}
	
	@PostMapping("/{companyId}")
	public CompanyDto addEmployeeToCompany(@RequestParam(required = false) Boolean change, @PathVariable long companyId, 
			@RequestBody List<EmployeeDto> employeeDtos) {
		
		if (change != null && change == true)			
			companies.get(companyId).getEmployeeDtos().clear();
		
		companies.get(companyId).getEmployeeDtos().addAll(employeeDtos);
		
		return companies.get(companyId);
	}

	@DeleteMapping("/{companyId}/{id}")
	public void deleteEmployeeFromCompany(@PathVariable long companyId, @PathVariable long id) {
		if (companies.get(companyId).getEmployeeDtos() != null)
			companies.get(companyId).getEmployeeDtos().remove(companies.get(companyId).
					getEmployeeDtos().indexOf(companies.get(companyId).getEmployeeDtos().
							stream().filter(c -> c.getId().equals(id)).findFirst().get()));
	}
	
}
