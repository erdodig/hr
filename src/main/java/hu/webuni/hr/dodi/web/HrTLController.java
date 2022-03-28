package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.dodi.model.Employee;

@Controller
public class HrTLController {
	
	private List<Employee> allEmployees = new ArrayList<>();
	
	{
		allEmployees.add(new Employee(1L, "Meta Flóra", "Secretary", 350000, LocalDateTime.now()));
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employees")
	public String listEmlpoyees(Map<String, Object> model) {
		model.put("employees", allEmployees);
		model.put("newEmployee", new Employee());
		return "employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee newEmployee) {
		newEmployee.setStartOfWork(LocalDateTime.now());
		allEmployees.add(newEmployee);
		return "redirect:employees";
	}
}
