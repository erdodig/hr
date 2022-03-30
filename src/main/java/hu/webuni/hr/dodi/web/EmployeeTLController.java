package hu.webuni.hr.dodi.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.webuni.hr.dodi.model.Employee;

@Controller
public class EmployeeTLController {
	
	private List<Employee> allEmployees = new ArrayList<>();
	
	{
		allEmployees.add(new Employee(1L, "Meta Flóra", "Secretary", 350000, LocalDateTime.now()));
		allEmployees.add(new Employee(2L, "Para Zita", "Teacher", 380000, LocalDateTime.now()));
		allEmployees.add(new Employee(3L, "Ebéd Elek", "Director", 420000, LocalDateTime.now()));
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employees")
	public String listEmlpoyees(@RequestParam(required = false) Long id, Map<String, Object> model) {
		
		if (id != null)			
			allEmployees.remove(allEmployees.indexOf(allEmployees.stream().filter(c -> c.getId().equals(id)).findFirst().get()));
			
		model.put("employees", allEmployees);
		model.put("newEmployee", new Employee());
		
		return "employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee newEmployee) {

		if (allEmployees.stream().filter(c -> c.getId().equals(newEmployee.getId())).findFirst().isPresent()) {
			
			allEmployees.set(
					allEmployees.indexOf(allEmployees.stream().filter(c -> c.getId().equals(newEmployee.getId())).findFirst().get()),
					newEmployee);
			
		} else { 	
			
			newEmployee.setStartOfWork(LocalDateTime.now());
			allEmployees.add(newEmployee);
		}
		
		return "redirect:employees";
	}
	
	@GetMapping("/employee")
	public String modifyEmployee(@RequestParam Long id, Map<String, Object> model) {
		model.put("modEmployee", allEmployees.stream().filter(c -> c.getId().equals(id)).findFirst().get());
		return "employee";
	}
	
}
