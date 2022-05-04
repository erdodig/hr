package hu.webuni.hr.dodi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private PositionRepository positionRepository;
	
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public Company update(Company company) {
		if(!companyRepository.existsById(company.getId()))
			return null;
		return companyRepository.save(company);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Optional<Company> findById(long id) {
		return companyRepository.findById(id);
	}

	public void delete(long id) {
		companyRepository.deleteById(id);
	}
	
	@Transactional
	public Company addEmployee(long id, Employee employee) {
		
		Company company = companyRepository.findByIdWithEmployees(id).get();
		company.addEmployee(employee);
		
		Position transientPosition = employee.getPosition();
		if(transientPosition != null) {
			List<Position> positionsByName = positionRepository.findByName(transientPosition.getName());
			if(positionsByName.isEmpty())
				throw new RuntimeException("position with this name does not exist in DB");
			
			Position positionInDb = positionsByName.get(0);
			employee.setPosition(positionInDb);
		}
		
		employeeRepository.save(employee);

		return company;
	}
	
	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		
		Company company = companyRepository.findByIdWithEmployees(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		
		employee.setCompany(null);
		employeeRepository.save(employee);

		company.getEmployees().remove(employee);
		
		/**
		 * a visszacsatolást csak így tudtam megoldani :(
		 */
		for (Employee employee2 : company.getEmployees()) {
			employee2.getPosition().getName();
		}
		
		return company;
	}
	
	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		
		Company company = companyRepository.findByIdWithEmployees(id).get();
		for (Employee employee : company.getEmployees()) {
			
			employee.setCompany(null);
			employee.setPosition(employeeRepository.findById(employee.getEmployeeId()).get().getPosition());
			employeeRepository.save(employee);
		}
		company.getEmployees().clear();
		
		for (Employee employee : employees) {
			
			employee.setCompany(company);
			employee.setPosition(employeeRepository.findById(employee.getEmployeeId()).get().getPosition());
			employeeRepository.save(employee);
		}
		company.setEmployees(employees);
	
		return company;
	}
	
}
