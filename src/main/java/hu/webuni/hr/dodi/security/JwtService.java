package hu.webuni.hr.dodi.security;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.dodi.config.HrConfigProperties;
import hu.webuni.hr.dodi.model.Employee;
import hu.webuni.hr.dodi.repository.UserRepository;

@Service
public class JwtService {
	
	@Autowired
	HrConfigProperties config;
	
	@Autowired
	UserRepository userRepository;

	private static final String AUTH = "auth";
	private static final String EMPLOYEES_LIST = "employeesList";
	private static final String LEADER_DATA = "leaderData";
	private static final String EMPLOYEE_DATA = "employeeData";

	public String createJwtToken(UserPrincipal principal) {
		
		return JWT.create()
				.withSubject(principal.getUsername())
				.withClaim(EMPLOYEE_DATA, Collections.singletonMap(
						principal.getEmployee().getName(), 
						principal.getEmployee().getEmployeeId())
						)
				.withClaim(LEADER_DATA, principal.getEmployee().getLeader() == null ? null
						:
						Collections.singletonMap(
						principal.getEmployee().getLeader().getUsername(), 
						principal.getEmployee().getLeader().getEmployeeId())
						)
				.withClaim(EMPLOYEES_LIST, principal.getEmployee().getEmployees() == null ? null
						:
						principal.getEmployee().getEmployees().stream()
						.collect(Collectors.toMap(Employee::getUsername, Employee::getEmployeeId))
						)
				.withArrayClaim(AUTH, principal.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).toArray(String[]::new))
				.withExpiresAt(new Date(System.currentTimeMillis() 
						+ TimeUnit.MINUTES.toMillis(config.getValidInMinutes())))
				.withIssuer(config.getIssuer())
				.sign(config.getAlgorithm());
				
	}

	public UserPrincipal parseJwt(String jwtToken) {
		
		DecodedJWT decodedJwt = JWT.require(config.getAlgorithm())
		.withIssuer(config.getIssuer())
		.build()
		.verify(jwtToken);
		
		Employee employee = userRepository.findByUsername(decodedJwt.getSubject()).get();
		
//		Employee employee = new Employee();
//		employee.setEmployeeId(decodedJwt.getClaim(EMPLOYEE_DATA).asMap());
		
		UserPrincipal userPrincipal = new UserPrincipal(employee, decodedJwt.getSubject(), employee.getPassword(), 
				decodedJwt.getClaim(AUTH).asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		
		return userPrincipal;
	}

}
