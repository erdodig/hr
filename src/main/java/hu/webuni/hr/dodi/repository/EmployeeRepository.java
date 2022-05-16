package hu.webuni.hr.dodi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.dodi.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Page<Employee> findBySalaryGreaterThan(Integer minSalary, Pageable pageable);

	List<Employee> findByPositionName(String jobTitle);
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);
	
	@Modifying
	@Query("UPDATE Employee e "
			+ "SET e.salary = :minSalary "
			+ "WHERE e.id IN("
			+ "SELECT (e2.id) "
			+ "FROM Employee e2 "
			+ "WHERE e2.position.name = :positionName "
			+ "AND e2.company.id = :companyId "
			+ "AND e2.salary < :minSalary"
			+ ")")
	void updateSalaries(String positionName, int minSalary, long companyId);


	List<Employee> findByName(String name);

	@EntityGraph(attributePaths = {"position", "roles", "company"})
	@Override
	public List<Employee> findAll();
	
	@EntityGraph(attributePaths = {"position", "roles", "company"})
	@Override
	public Optional<Employee> findById(Long id);
}
