package hu.webuni.hr.dodi.web;

import java.util.List;
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

import hu.webuni.hr.dodi.dto.CompanyDto;
import hu.webuni.hr.dodi.dto.EmployeeDto;
import hu.webuni.hr.dodi.mapper.CompanyMapper;
import hu.webuni.hr.dodi.mapper.EmployeeMapper;
import hu.webuni.hr.dodi.model.AverageSalaryByPosition;
import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	private CompanyMapper companyMapper;
	private CompanyService companyService;
	private EmployeeMapper employeeMapper;
	private CompanyRepository companyRepository;
	

	public CompanyController(CompanyMapper companyMapper, CompanyService companyService, EmployeeMapper employeeMapper,
			CompanyRepository companyRepository) {
		this.companyMapper = companyMapper;
		this.companyService = companyService;
		this.employeeMapper = employeeMapper;
		this.companyRepository = companyRepository;
	}

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
		
		List<Company> companies = null;
		
		boolean notfull = (full == null || !full);
		if (notfull) 			
			companies = companyService.findAll();
		else {
			companies = companyRepository.findAllWithEmployees();
			
			/**
			 * nem sz??p megold??s de ezt m??shogy nem tudtam megoldani, hogy a position bet??lt??dj??n
			 * pr??b??ltam a CompanyRepository-ban mindennel, de nem siker??lt 
			 */
			for (Company company : companies) {
				for (Employee employee : company.getEmployees()) {
					employee.setPosition(employeeRepository.findById(employee.getEmployeeId()).get().getPosition());
				}
			}
		}
		
		return mapCompanies(companies, !notfull);
	}
	
	private List<CompanyDto> mapCompanies(List<Company> companies, Boolean full) {
		if(isFull(full)) {
			return companyMapper.companiesToDtos(companies);
		} else {
			return companyMapper.companiesToDtosNoEmployees(companies);
		}
	}

	private boolean isFull(Boolean full) {
		return full != null && full;
	}

	@GetMapping("/{id}")
	public CompanyDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
		
		Company company = companyService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(isFull(full))
			return companyMapper.companyToDto(company);
		else
			return companyMapper.companyToDtoNoEmployees(company);
	}

	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
      return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
      companyDto.setId(id);
      Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
      if (updatedCompany == null) {
          return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok(companyMapper.companyToDto(updatedCompany));
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
      companyService.delete(id);
	}

	@PostMapping("/{companyId}/employees")
	public CompanyDto addNewEmployee(@PathVariable long companyId, @RequestBody EmployeeDto employeeDto) {
		try {
			return companyMapper
					.companyToDto(companyService.addEmployee(companyId, employeeMapper.dtoToEmployee(employeeDto)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{companyId}/employees/{empId}")
	public CompanyDto deleteEmployee(@PathVariable long companyId, @PathVariable long empId) {
		
		try {
			
			Company company = companyService.deleteEmployee(companyId, empId);
			return companyMapper.companyToDto(company);
			
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{companyId}/employees")
	public CompanyDto replaceEmployees(@PathVariable long companyId, @RequestBody List<EmployeeDto> newEmployees) {
		try {
			return companyMapper
					.companyToDto(companyService.replaceEmployees(companyId, employeeMapper.dtosToEmployees(newEmployees)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping(params = "aboveSalary")
    public List<CompanyDto> getCompaniesAboveASalary(@RequestParam int aboveSalary,
            @RequestParam(required = false) Boolean full) {
        List<Company> allCompanies = companyRepository.findByEmployeeWithSalaryHigherThan(aboveSalary);
        return mapCompanies(allCompanies, full);
    }
	
	
	@GetMapping(params = "aboveEmployeeNumber")
	public List<CompanyDto> getCompaniesAboveEmployeeNumber(@RequestParam int aboveEmployeeNumber,
			@RequestParam(required = false) Boolean full) {
		List<Company> filteredCompanies = companyRepository.findByEmployeeCountHigherThan(aboveEmployeeNumber);
		return mapCompanies(filteredCompanies, full);
	}
	
	@GetMapping("/{id}/salaryStats")
	public List<AverageSalaryByPosition> getSalaryStatsById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
		return companyRepository.findAverageSalariesByPosition(id);
	}
}
