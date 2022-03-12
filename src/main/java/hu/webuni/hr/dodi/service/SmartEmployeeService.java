package hu.webuni.hr.dodi.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {
	
	@Autowired
	HrConfigProperties config;
	
	@Override
	public int getPayRaisePercent(Employee employee) {
		
		double years = (LocalDateTime.now().getYear() - employee.getStartOfWork().getYear())
						+ (employee.getStartOfWork().getMonthValue() / 12.0);
		
		if (years >= config.getEmployee().getSmart().getLimit10()) {
			
			return config.getEmployee().getSmart().getPercent10();
			
		} else if (years >= config.getEmployee().getSmart().getLimit5()) {
			
			return config.getEmployee().getSmart().getPercent5();
			
		} else if (years >= config.getEmployee().getSmart().getLimit2()) {
			
			return config.getEmployee().getSmart().getPercent2();
			
		} else {
			
			return 0;
		}
		
	}

	/**
	 * @Value verzió
	 */
	
//	@Value("${hr.employee.smart.limit10}")
//	private int limit10;
//	
//	@Value("${hr.employee.smart.percent10}")
//	private int percent10;
//
//	@Value("${hr.employee.smart.limit5}")
//	private int limit5;
//
//	@Value("${hr.employee.smart.percent5}")
//	private int percent5;
//
//	@Value("${hr.employee.smart.limit2}")
//	private double limit2;
//
//	@Value("${hr.employee.smart.percent2}")
//	private int percent2;
//	
//	@Override
//	public int getPayRaisePercent(Employee employee) {
//		
//		double years = (LocalDateTime.now().getYear() - employee.getStartOfWork().getYear())
//						+ (employee.getStartOfWork().getMonthValue() / 12.0);
//		
//		if (years >= limit10) {
//			
//			return percent10;
//			
//		} else if (years >= limit5) {
//			
//			return percent5;
//			
//		} else if (years >= limit2) {
//			
//			return percent2;
//			
//		} else {
//			
//			return 0;
//		}
//		
//	}

}
