package hu.webuni.hr.dodi.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.UserRepository;

@Service
public class HrUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Employee employee = userRepository.findByUsername(username)
				.orElseThrow(() -> new  UsernameNotFoundException(username));
		
		return new UserPrincipal(employee, employee.getUsername(), employee.getPassword(), employee.getRoles().stream()
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}

}
