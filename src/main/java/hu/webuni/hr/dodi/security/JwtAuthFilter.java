package hu.webuni.hr.dodi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import hu.webuni.hr.dodi.repository.UserRepository;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION = "Authorization";
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith(BEARER)) {
			
			String jwtToken = authHeader.substring(BEARER.length());
			UserPrincipal principal = jwtService.parseJwt(jwtToken);
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					principal, null, principal.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder .getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}

}
