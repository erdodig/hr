package hu.webuni.hr.dodi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {

	Employee employee = new Employee();

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public static class Employee {
		
		private Def def = new Def();
		
		private Smart smart = new Smart();

		public Def getDef() {
			return def;
		}

		public void setDef(Def def) {
			this.def = def;
		}

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart smart) {
			this.smart = smart;
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
		
		private int limit10;
		
		private int percent10;
		
		private int limit5;
		
		private int percent5;
		
		private double limit2;
		
		private int percent2;

		public int getLimit10() {
			return limit10;
		}

		public void setLimit10(int limit10) {
			this.limit10 = limit10;
		}

		public int getPercent10() {
			return percent10;
		}

		public void setPercent10(int percent10) {
			this.percent10 = percent10;
		}

		public int getLimit5() {
			return limit5;
		}

		public void setLimit5(int limit5) {
			this.limit5 = limit5;
		}

		public int getPercent5() {
			return percent5;
		}

		public void setPercent5(int percent5) {
			this.percent5 = percent5;
		}

		public double getLimit2() {
			return limit2;
		}

		public void setLimit2(double limit2) {
			this.limit2 = limit2;
		}

		public int getPercent2() {
			return percent2;
		}

		public void setPercent2(int percent2) {
			this.percent2 = percent2;
		}
		
	}
	
}
