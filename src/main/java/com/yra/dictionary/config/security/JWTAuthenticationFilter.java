package com.yra.dictionary.config.security;

import static com.yra.dictionary.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.yra.dictionary.config.security.SecurityConstants.HEADER_STRING;
import static com.yra.dictionary.config.security.SecurityConstants.SECRET;
import static com.yra.dictionary.config.security.SecurityConstants.TOKEN_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yra.dictionary.model.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
																							HttpServletResponse res) throws AuthenticationException {
		try {
			Account account = new ObjectMapper()
							.readValue(req.getInputStream(), Account.class);

			return authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(
											account.getEmail(),
											account.getPassword(),
											new ArrayList<>())
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
																					HttpServletResponse res,
																					FilterChain chain,
																					Authentication auth) throws IOException, ServletException {

		String token = Jwts.builder()
						.setSubject(((User) auth.getPrincipal()).getUsername())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET)
						.compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
}
