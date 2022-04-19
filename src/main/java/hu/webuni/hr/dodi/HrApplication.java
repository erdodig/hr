package hu.webuni.hr.dodi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.model.Company;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.CompanyRepository;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.service.InitDbService;
import hu.webuni.hr.dodi.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;
	
	@Autowired
	HrConfigProperties config;
	
	@Autowired
	InitDbService initDbService;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {

		initDbService.clearDB();
		
		initDbService.insertTestData();
		
		int salary = 420000;
		List<Company> companies = companyRepository.findCompaniesWhereThereHigherSalaryEmployees(salary);		
		System.out.format("A következő cégek rendelkeznek %d -nál nagyobb fizetésű dolgozókkal: %s%n", salary, companies);
		
		long count = 2;
		companies = companyRepository.findCompaniesWhereEmployeesCountMoreThan(count);
		System.out.format("A következő cégeknél dolgoznak többen mint %d: %s", count, companies);
			
		Page<Employee> employees = employeeRepository.findAll(PageRequest.of(0, 5));
		for (Employee employee : employees) {
			System.out.println(employee.getCompany().getName() + ": " + employee.getName());
		}
		
		
		

//		Smart smartConfig = config.getSalary().getSmart();
//		for (Double limit : 
//				smartConfig.getLimits().keySet()) {
//			
//			int origSalary = 100;
//			LocalDateTime limitDay = LocalDateTime.now().minusDays((long)(limit*365));
//			Employee e1 = new Employee(1L, "Nagy Péter", "fejlesztő", origSalary, limitDay.plusDays(1), null);
//			Employee e2 = new Employee(2L, "Kis Gábor", "projektmenedzser", origSalary, limitDay.minusDays(1), null);
//
//			salaryService.setNewSalary(e1);
//			salaryService.setNewSalary(e2);
//
//			System.out.format("1 nappal a %.2f éves határ előtt az új fizetés %d%n", limit, e1.getSalary());
//			System.out.format("1 nappal a %.2f éves határ után az új fizetés %d%n", limit, e2.getSalary());
//		}
		
	}


}
