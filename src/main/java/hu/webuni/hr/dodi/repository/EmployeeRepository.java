package hu.webuni.hr.dodi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByJobTitle(String jobTitle);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);
}
