package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.model.PositionDetailsByCompany;
import hu.webuni.hr.dodi.model.Qualification;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionDetailsByCompanyRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;
import hu.webuni.hr.dodi.repository.UserRepository;

@Service
public class InitDbService {

	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;
	
	public InitDbService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void initDb() {
		
		Position developer = positionRepository.save(new Position("fejlesztő", Qualification.UNIVERSITY));
		Position tester = positionRepository.save(new Position("tesztelő", Qualification.HIGH_SCHOOL));

		Employee newEmployee3 = new Employee(null, "Leader Péter",400000, LocalDateTime.now()
				, "leader", passwordEncoder.encode("pass"), Set.of("admin", "user"), null);
		newEmployee3.setPosition(developer);
		employeeRepository.save(newEmployee3);
		
		Employee newEmployee1 = new Employee(null, "Kiss János", 300000, LocalDateTime.now()
				, "kiss", passwordEncoder.encode("pass"), Set.of("user"), newEmployee3);
		newEmployee1.setPosition(developer);
		newEmployee1.setLeader(newEmployee3);
		employeeRepository.save(newEmployee1);
		
		Employee newEmployee2 = new Employee(null, "Nagy János",350000, LocalDateTime.now()
				, "nagy", passwordEncoder.encode("pass"), Set.of("user"), newEmployee3);
		newEmployee2.setPosition(tester);
		newEmployee2.setLeader(newEmployee3);
		employeeRepository.save(newEmployee2);
		
		Company newCompany = companyRepository.save(new Company(null, 10, "Fa vágó Kft.", "", null));
		newCompany.addEmployee(newEmployee3);
		newCompany.addEmployee(newEmployee2);
		newCompany.addEmployee(newEmployee1);
		
		PositionDetailsByCompany pd = new PositionDetailsByCompany();
		pd.setCompany(newCompany);
		pd.setMinSalary(250000);
		pd.setPosition(developer);
		positionDetailsByCompanyRepository.save(pd);
		
		PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
		pd2.setCompany(newCompany);
		pd2.setMinSalary(200000);
		pd2.setPosition(tester);
		positionDetailsByCompanyRepository.save(pd2);
	}
}
