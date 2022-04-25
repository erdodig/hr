package hu.webuni.hr.dodi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.EmployeeRepository;
import hu.webuni.hr.dodi.repository.PositionDetailsByCompanyRepository;
import hu.webuni.hr.dodi.repository.PositionRepository;

@Service
public class SalaryService {

	private EmployeeService employeeService;
	private PositionRepository positionRepository;
	private PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	private EmployeeRepository employeeRepository;
	
	public SalaryService(EmployeeService employeeService, PositionRepository positionRepository,
			PositionDetailsByCompanyRepository positionDetailsByCompanyRepository,
			EmployeeRepository employeeRepository) {
		this.employeeService = employeeService;
		this.positionRepository = positionRepository;
		this.positionDetailsByCompanyRepository = positionDetailsByCompanyRepository;
		this.employeeRepository = employeeRepository;
	}


	public void setNewSalary(Employee employee) {
		int newSalary = employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100;
		employee.setSalary(newSalary);
	}
	
//	@Transactional
//	public void raiseMinSalary(String positionName, int minSalary) {
//		//nem a leghatékonyabb: sok employee esetén egyesével futnak majd az UPDATE utasítások
//		positionRepository.findByName(positionName)
//		.forEach(p -> {
//			p.setMinSalary(minSalary);
//			p.getEmployees().forEach(e ->{
//				if(e.getSalary() < minSalary)
//					e.setSalary(minSalary);
//			});
//		});
//	}

	@Transactional
	public void raiseMinSalary(long companyId, String positionName, int minSalary) {
		positionDetailsByCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId)
			.forEach(pd ->{
				pd.setMinSalary(minSalary);
				//nem hatékony: külön update minden employee-ra
//				pd.getCompany().getEmployees().forEach(e ->{
//					if(e.getSalary() < minSalary && e.getPosition().getName().equals(positionName))
//						e.setSalary(minSalary);
//				});
			});
		employeeRepository.updateSalaries(positionName, minSalary, companyId);
	}
}
