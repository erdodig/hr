package hu.webuni.hr.dodi.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CompanyType {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 10)
	private String companyType;

	public CompanyType() {
	}

	public CompanyType(String companyType) {
		this.companyType = companyType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
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
		CompanyType other = (CompanyType) obj;
		return Objects.equals(id, other.id);
	}

}
