package hu.webuni.hr.dodi.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.EmployeeRepository;

@Service
public abstract class AbstractEmployeeService implements EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		
		if(!employeeRepository.existsById(employee.getId()))
			return null;
		
		return employeeRepository.save(employee);
	}
	
	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
	
	@Override
	public Optional<Employee> findByid(Long id) {
		return employeeRepository.findById(id);
	}
	
	@Override
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
}
