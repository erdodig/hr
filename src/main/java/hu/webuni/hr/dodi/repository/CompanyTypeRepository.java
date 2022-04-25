package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.CompanyType;

public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {

	CompanyType findByCompanyType(String companyType);
}
