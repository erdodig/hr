package hu.webuni.hr.dodi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Company {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private int registrationNumber;
	
	private String name;
	
	private String address;
	
//	@Enumerated(EnumType.STRING)
//	@Column(length = 10)
	@ManyToOne()
	private CompanyType companyType;
	
	@OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("id")
	private List<Employee> employees = new ArrayList<>();
	
	public Company() {
		
	}

	public Company(int registrationNumber, String name, String adress) {
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = adress;
	}

	public int getRegistrationNumber() {
		return registrationNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRegistrationNumber(int registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	public void addEmployee(Employee employee) {
		employee.setCompany(this);
		if(this.employees == null)
			this.employees = new ArrayList<>();
		this.employees.add(employee);
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", registrationNumber=" + registrationNumber + ", name=" + name + ", address="
				+ address + ", companyType=" + companyType.getCompanyType() + ", employees=" + employees + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(id, other.id);
	}

}
