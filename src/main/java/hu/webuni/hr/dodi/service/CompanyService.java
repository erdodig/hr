package hu.webuni.hr.dodi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Company;

@Service
public class CompanyService {

	private Map<Long, Company> companies = new HashMap<>();
	{
		companies.put(1L, new Company(1L, 123543, "Kiss Co.", "1030 Budapest, Akárhol utca 12.", null));
		companies.put(2L, new Company(2L, 987654, "Nagy Co.", "7630 Pécs, Nagy utca 27.", null));
	}

	public Company save(Company company) {
		companies.put(company.getId(), company);
		return company;
	}
	
	public List<Company> findAll() {
		return new ArrayList<>(companies.values());
	}
	
	public Company findByid(long id) {
		return companies.get(id);
	}
	
	public void delete(long id) {
		companies.remove(id);
	}

	public Company mapCompanyWithoutEmployees(Company c) {
		return new Company(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
	}

}
