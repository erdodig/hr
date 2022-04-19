package hu.webuni.hr.dodi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import hu.webuni.hr.dodi.model.Employee;

//public interface EmployeeRepository extends JpaRepository<Employee, Long> {
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

	List<Employee> findByJobTitle(String jobTitle);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);
	
	Page<Employee> findAll(Pageable pageable);

//	@Query("SELECT e, e.jobTitle, AVG(e.salary) "
//			+ "FROM Employee e "
//			+ "WHERE e.company.id = ?1 "
//			+ "GROUP BY e, e.jobTitle")
//	List<Employee> findByJobtitleWithAverageSalary(Long companyId);
	
}
