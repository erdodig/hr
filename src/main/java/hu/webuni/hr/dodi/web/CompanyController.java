package hu.webuni.hr.dodi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
		
		List<Company> companies = companyService.findAll();
		
		if (full)		
			return companyMapper.companiesToDtos(companyService.findAll());
		else
			return companyMapper.companiesToDtos(companyService.clearEmployees(companies));
	}
	

	@GetMapping("/{id}")
	public CompanyDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {

		Company company = companyService.findByid(id).get();
		
		if(full == null || !full) {
			return companyMapper.companyToDto(companyService.clearEmployee(company));
		} else
			return companyMapper.companyToDto(company);
	}
	
	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
				
		companyDto.setId(id);
		
		Company company = companyService.update(companyMapper.dtoToCompany(companyDto));
		
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}
	
	@PostMapping("/{id}/employees")
	public CompanyDto addEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		
		try {
			
			return companyMapper
					.companyToDto(companyService.addEmployee(id, employeeMapper.dtoToEmployee(employeeDto)));
			
		} catch (NoSuchElementException e) {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		
		try {
			
			return companyMapper.companyToDto(companyService.deleteEmployee(id, employeeId));
			
		} catch (NoSuchElementException e) {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto replaceAllEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> employees) {
		
		try {
			
			return companyMapper
					.companyToDto(companyService.replaceEmployees(id, employeeMapper.dtosToEmployees(employees)));
			
		} catch (NoSuchElementException e) {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
