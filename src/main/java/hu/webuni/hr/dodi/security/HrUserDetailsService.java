package hu.webuni.hr.dodi.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.dodi.model.HrUser;
import hu.webuni.hr.dodi.repository.UserRepository;

@Service
public class HrUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		HrUser hrUser = userRepository.findById(username)
				.orElseThrow(() -> new  UsernameNotFoundException(username));
		return new User(username, hrUser.getPassword(), hrUser.getRoles().stream()
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}

}
