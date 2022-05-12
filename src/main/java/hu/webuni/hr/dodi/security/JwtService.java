package hu.webuni.hr.dodi.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.GrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtService {

	public String createJwtToken(UserPrincipal principal) {
		
		return JWT.create()
				.withSubject(principal.getUsername())
				.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
				.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2)))
				.withIssuer("HrApp")
				.sign(Algorithm.HMAC256("mysecret"));
	}

}
