package hu.webuni.hr.dodi.security;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTCreationException;

import hu.webuni.hr.dodi.dto.LoginDto;

@RestController
public class JwtLoginController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;

	@PostMapping("/api/login")
	public String login(@RequestBody LoginDto loginDto) throws IllegalArgumentException, JWTCreationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		
		return jwtService.createJwtToken((UserPrincipal) authentication.getPrincipal());
	}
}
