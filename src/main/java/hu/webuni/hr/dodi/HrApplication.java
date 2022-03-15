package hu.webuni.hr.dodi;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {
	
	@Autowired
	SalaryService salaryService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Zoli: " + salaryService.getPayRaisePercent(
				new Employee(LocalDateTime.of(2017, 2, 13, 8, 0))));
		
		System.out.println("Kata: " + salaryService.getPayRaisePercent(
				new Employee(LocalDateTime.of(2010, 2, 13, 8, 0))));
		
		System.out.println("Peti: " + salaryService.getPayRaisePercent(
				new Employee(LocalDateTime.of(2021, 8, 13, 8, 0))));
		
		System.out.println("Flóra: " + salaryService.getPayRaisePercent(
				new Employee(LocalDateTime.of(2020, 6, 13, 8, 0))));
	}

}
