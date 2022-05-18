package hu.webuni.hr.dodi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import hu.webuni.hr.dodi.model.Employee;

public class UserPrincipal extends User {

	private static final long serialVersionUID = -7932722935540701279L;

	private Employee employee; 
	
//	private Collection<? extends GrantedAuthority> authorities;
	
//	public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//		super(username, password, authorities);
//	}
	
	public UserPrincipal(Employee employee, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.employee = employee;
	}

//	public UserPrincipal(Employee employee, Collection<? extends GrantedAuthority> authorities) {
//		this.employee = employee;
//		this.authorities = authorities;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return authorities;
//	}

//	@Override
//	public String getPassword() {
//		return employee.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return employee.getUsername();
//	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}

}
