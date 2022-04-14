package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.Company;


public interface CompanyRepository extends JpaRepository<Company, Long> {

}
