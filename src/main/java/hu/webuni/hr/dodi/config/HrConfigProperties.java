package hu.webuni.hr.dodi.config;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {

	Employee employee = new Employee();
	
	@PostConstruct
	private void sorting() {
		employee.getSmarts().sort(Comparator.comparing(Smart::getLimit).reversed());
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public static class Employee {
		
		private Def def = new Def();
		
		private List<Smart> smarts;

		public Def getDef() {
			return def;
		}

		public void setDef(Def def) {
			this.def = def;
		}

		public List<Smart> getSmarts() {
			return smarts;
		}

		public void setSmarts(List<Smart> smarts) {
			this.smarts = smarts;
		}
		
	}
	
	public static class Def {
	
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}
		
	}
	
	public static class Smart {
		
		private int limit;
		
		private int percent;

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

	}
	
}
