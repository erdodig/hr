package hu.webuni.hr.dodi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private Long employeeId;
	
	private String name;
	
	private int salary;
	
	private LocalDateTime dateOfStartWork;
	
	@ManyToOne
	private Company company;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Position position;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Employee leader;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Employee> employees = new ArrayList<>();
	
	public Employee() {
	}

	public Employee(int salary, LocalDateTime dateOfStartWork) {
		this.salary = salary;
		this.dateOfStartWork = dateOfStartWork;
	}

	public Employee(Long employeeId, String name, int salary, LocalDateTime dateOfStartWork) {
//		this(employeeId, name, salary, dateOfStartWork);
		this.employeeId = employeeId;
		this.name = name;
		this.salary = salary;
		this.dateOfStartWork = dateOfStartWork;
	}

	public Employee(Long employeeId, String name, int salary, LocalDateTime dateOfStartWork, 
			String username, String password, Set<String> roles, Employee leader) {
		this.employeeId = employeeId;
		this.name = name;
		this.salary = salary;
		this.dateOfStartWork = dateOfStartWork;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.leader = leader;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getDateOfStartWork() {
		return dateOfStartWork;
	}

	public void setDateOfStartWork(LocalDateTime dateOfStartWork) {
		this.dateOfStartWork = dateOfStartWork;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void addEmployee(Employee employee) {
		employee.setLeader(this);
		if(this.employees == null)
			this.employees = new ArrayList<>();
		this.employees.add(employee);		
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(employeeId, other.employeeId);
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", name=" + name + ", salary=" + salary + ", dateOfStartWork="
				+ dateOfStartWork + ", company=" + company + ", position=" + position + "]";
	}
	
}
