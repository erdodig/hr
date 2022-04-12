package hu.webuni.hr.dodi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.dodi.dto.CompanyDto;
import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.dto.View;
import hu.webuni.hr.dodi.mapper.CompanyMapper;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@GetMapping(params = "full=true")
	public List<CompanyDto> getAllFull() {
		return companyMapper.companiesToDtos(companyService.findAll());
	}
	

	@JsonView(View.BaseData.class)
	@GetMapping
	public List<CompanyDto> getAllNonFull(){
		return companyMapper.companiesToDtos(companyService.findAll());
	}	
	
	@GetMapping("/{id}")
	public CompanyDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {

		Company fullCompany = companyService.findByid(id);
		
		if(full == null || !full) {
			return companyMapper.companyToDto(companyService.mapCompanyWithoutEmployees(fullCompany));
		} else
			return companyMapper.companyToDto(fullCompany);
	}
	
	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
		
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		
		return companyMapper.companyToDto(company);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
				
		if (!companyService.findAll().stream().anyMatch(c -> c.getId() == id))
			return ResponseEntity.notFound().build();
		
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}
	
	@PostMapping("/{id}/employees")
	public CompanyDto addEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
		
		Company company = getCompanyOrThrowNotFound(id);
		
		company.getEmployees().add(employeeMapper.dtoToEmployee(employee));
		
		return companyMapper.companyToDto(company);
	}


	private Company getCompanyOrThrowNotFound(long id) {
		
		Company company = companyService.findByid(id);
		
		if(company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		return company;
	}
	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		
		CompanyDto companyDto = companyMapper.companyToDto(getCompanyOrThrowNotFound(id));
		
		companyDto.getEmployees().removeIf(e -> e.getId() == employeeId);
		
		return companyDto;
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto replaceAllEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		
		CompanyDto companyDto = companyMapper.companyToDto(getCompanyOrThrowNotFound(id));
		
		companyDto.setEmployees(employees);
		
		return companyDto;
	}
	
	
}
