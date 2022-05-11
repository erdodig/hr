package hu.webuni.hr.dodi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.Employee;

public interface UserRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByUsername(String username);

}
