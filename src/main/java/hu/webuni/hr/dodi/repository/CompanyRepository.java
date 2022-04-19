package hu.webuni.hr.dodi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.dodi.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT DISTINCT c "
			+ "FROM Company c "
			+ "JOIN FETCH Employee e ON e.company.id = c.id "
			+ "WHERE e.salary > ?1")
	List<Company> findCompaniesWhereThereHigherSalaryEmployees(int salary);
	
	@Query("SELECT c "
			+ "FROM Company c "
			+ "JOIN FETCH Employee e ON e.company.id = c.id "
			+ "GROUP BY 1 "
			+ "HAVING COUNT(1) > ?1 "
			+ "ORDER BY c.id")
	List<Company> findCompaniesWhereEmployeesCountMoreThan(long count);
}
