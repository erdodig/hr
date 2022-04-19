package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.CompanyType;
import hu.webuni.hr.dodi.model.Education;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Position;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.CompanyTypeRepository;
import hu.webuni.hr.dodi.repository.EducationRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private EducationRepository educationRepository;

	public void clearDB() {
	
		companyRepository.deleteAll();
		
		employeeRepository.deleteAll();
		
		companyTypeRepository.deleteAll();
		
		positionRepository.deleteAll();
		
		educationRepository.deleteAll();
		
	}
	
	public void insertTestData() {
		
		/**
		 * Iskolai végzettség
		 */
		Education education1 = new Education("érettségi");
		education1 = educationRepository.save(education1);
		
		Education education2 = new Education("főiskola");
		education2 = educationRepository.save(education2);
		
		Education education3 = new Education("egyetem");
		education3 = educationRepository.save(education3);
		
		Education education4 = new Education("PhD");
		education4 = educationRepository.save(education4);

		
		/**
		 * Pozíciók
		 */
		Position position1 = new Position("secretary", education1, 280000);
		position1 = positionRepository.save(position1);
		
		Position position2 = new Position("developer", education2, 450000);
		position2 = positionRepository.save(position2);

		Position position3 = new Position("porter", null, 250000);
		position3 = positionRepository.save(position3);

		Position position4 = new Position("teacher", education3, 400000);
		position4 = positionRepository.save(position4);
		

		/**
		 * Cég típusa
		 */
		CompanyType companyType1 = new CompanyType("BT");
		companyTypeRepository.save(companyType1);
		
		CompanyType companyType2 = new CompanyType("KFT");
		companyTypeRepository.save(companyType2);
		
		CompanyType companyType3 = new CompanyType("ZRT");
		companyTypeRepository.save(companyType3);
		
		CompanyType companyType4 = new CompanyType("NYRT");
		companyTypeRepository.save(companyType4);
		

		/**
		 * Cégek
		 */
		Company company1 = new Company(12345678, "Ágvágó Kft.", "7630 Pécs, Tüskésréti u. 42.");
		company1.setCompanyType(companyTypeRepository.findByCompanyType("KFT"));
		company1 = companyRepository.save(company1);
		
		Employee employee1 = new Employee("Kiss Sándor", position3, 240000, LocalDateTime.of(2011, 4, 1, 8, 0, 0), company1);
		company1.getEmployees().add(employee1);
		
		Employee employee11 = new Employee("Kiss János", position3, 260000, LocalDateTime.of(2011, 4, 1, 8, 0, 0), company1);
		company1.getEmployees().add(employee11);
		
		Employee employee2 = new Employee("Meta Flóra", position1, 320000, LocalDateTime.of(2015, 7, 8, 10, 0, 0), company1);
		company1.getEmployees().add(employee2);
		
		Employee employee21 = new Employee("Meta Márta", position1, 300000, LocalDateTime.of(2015, 7, 8, 10, 0, 0), company1);
		company1.getEmployees().add(employee21);
		
		Employee employee3 = new Employee("Nagy János", position2, 450000, LocalDateTime.of(2020, 10, 1, 9, 0, 0), company1);
		company1.getEmployees().add(employee3);
		
		Employee employee31 = new Employee("Nagy Zoltán", position2, 530000, LocalDateTime.of(2020, 10, 1, 9, 0, 0), company1);
		company1.getEmployees().add(employee31);
		
		companyRepository.save(company1);

		
		Company company2 = new Company(12345678, "Mekk Elek Bt.", "1015 Budapest, Rákóczi út. 105.");
		company2.setCompanyType(companyTypeRepository.findByCompanyType("BT"));
		company2 = companyRepository.save(company2);
		
		Employee employee4 = new Employee("Nagy Sándor", position4, 400000, LocalDateTime.of(2008, 1, 1, 8, 0, 0), company2);
		company2.getEmployees().add(employee4);
		
		Employee employee41 = new Employee("Nagy Péter", position4, 440000, LocalDateTime.of(2008, 1, 1, 8, 0, 0), company2);
		company2.getEmployees().add(employee41);
		
		Employee employee5 = new Employee("Patta Nóra", position1, 320000, LocalDateTime.of(2015, 7, 8, 10, 0, 0), company2);
		company2.getEmployees().add(employee5);
		
		companyRepository.save(company2);
		
	}
	
}
