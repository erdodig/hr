package hu.webuni.hr.dodi.model;

import java.time.LocalDateTime;

public class Employee {

	private Long id;
	
	private String name;
	
	private String job;
	
	private int salary;
	
	private LocalDateTime startOfWork;

	public Employee(LocalDateTime startOfWork) {
		this.startOfWork = startOfWork;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getStartOfWork() {
		return startOfWork;
	}

	public void setStartOfWork(LocalDateTime startOfWork) {
		this.startOfWork = startOfWork;
	}
	
	
}
