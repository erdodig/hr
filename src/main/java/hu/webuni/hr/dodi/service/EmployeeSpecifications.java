package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.model.Employee_;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(long id) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.employeeId), id);
	}

	public static Specification<Employee> hasEmployeeName(String employeeName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.name)), employeeName.toLowerCase() + "%");
	}

	public static Specification<Employee> hasEntryDate(LocalDateTime entryDate) {
		LocalDateTime startOfDay = LocalDateTime.of(entryDate.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Employee_.dateOfStartWork), startOfDay, startOfDay.plusDays(1));
	}

	public static Specification<Employee> hasPositionName(String positionName) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.position.getName()), positionName);
	}

	public static Specification<Employee> hasCompanyName(String companyName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.company.getName())), companyName.toLowerCase() + "%");
	}

	public static Specification<Employee> hasSalary(int salary) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.salary), salary);
	}	
	
}
