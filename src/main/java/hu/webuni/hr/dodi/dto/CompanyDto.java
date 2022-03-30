package hu.webuni.hr.dodi.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {

	private Long id;
	
	private String companyRegistrationNumber;
	
	private String companyName;
	
	private String companyAddress;
	
	private List<EmployeeDto> employeeDtos = new ArrayList<>();

	public CompanyDto() {
	}

	public CompanyDto(Long id, String companyRegistrationNumber, String companyName, String companyAddress) {
		this.id = id;
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.companyName = companyName;
		this.companyAddress = companyAddress;
	}

	public CompanyDto(Long id, String companyRegistrationNumber, String companyName, String companyAddress,
			List<EmployeeDto> employeeDtos) {
		this.id = id;
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.companyName = companyName;
		this.companyAddress = companyAddress;
		this.employeeDtos = employeeDtos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}

	public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public List<EmployeeDto> getEmployeeDtos() {
		return employeeDtos;
	}

	public void setEmployeeDtos(List<EmployeeDto> employeeDtos) {
		this.employeeDtos = employeeDtos;
	}	
	
}
