package com.cog.hotel.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cog.hotel.model.CustomerDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	@Value("${secretkey}")
	private String SECRETKEY;

	@Value("${tokenduration}")
	private int TOKENDURATION;

	public String extractEmailAddress(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		String formated_token = token.trim().replaceAll("\0xfffd", "");
		return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(formated_token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(CustomerDetails customerDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, customerDetails.getEmailAddress());
	}

	private String createToken(Map<String, Object> claims, String emailAddress) {

		return Jwts.builder().setClaims(claims).setSubject(emailAddress)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKENDURATION * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();
	}

	public Boolean validateToken(String token, CustomerDetails customerDetails) {
		final String emailAddress = extractEmailAddress(token);
		return (emailAddress.equals(customerDetails.getUsername()) && !isTokenExpired(token));
	}
}