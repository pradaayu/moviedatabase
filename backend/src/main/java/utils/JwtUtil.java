package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
	@Value("${jwt.secret.key}") 
	private String secretKey; // Secret key for signing the JWT
	private static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes
	private static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 7 days
	
	/**
	 * 
	 * @return Secret key object for signing the JWT
	 */
	private Key getKey() {
	    return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	/**
	 * Generates a JWT token with custom claims (including userId).
	 * 
	 * @param email the user's email address (used for standard authentication)
	 * @param userId the user's unique identifier (to be included in the claims)
	 * @return the generated JWT token
	 */
	public String generateAccessToken(String email, String userId) {
		return Jwts.builder()
			.setSubject(email)
			.claim("userId", userId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
			.signWith(getKey(), SignatureAlgorithm.HS256) // Use the Key object for signing
			.compact();
	}

	/**
	 * Generates a JWT refresh token with custom claims (including userId).
	 * 
	 * @param email the user's email address (used for standard authentication)
	 * @param userId the user's unique identifier (to be included in the claims)
	 * @return the generated JWT refresh token
	 */
	public String generateRefreshToken(String email, String userId) {
		return Jwts.builder()
			.setSubject(email)
			.claim("userId", userId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME)) // Longer expiration for refresh token
			.signWith(getKey(), SignatureAlgorithm.HS256) // Use the Key object for signing
			.compact();
	}
	
	/**
	 * Extracts the email from the token.
	 * 
	 * @param token the JWT token
	 * @return the email contained in the token
	 */
	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}
	
	/**
	 * Extracts the userId from the token's claims.
	 * 
	 * @param token the JWT token
	 * @return the userId contained in the token
	 */
	public String extractUserId(String token) {
		return (String) extractClaims(token).get("userId");
	}
	
	/**
	 * Extracts all claims from the JWT token.
	 * 
	 * @param token the JWT token
	 * @return the claims in the token
	 */
	private Claims extractClaims(String token) {	
		return Jwts.parserBuilder()
			.setSigningKey(getKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * Validates the JWT token.
	 * 
	 * @param token the JWT token
	 * @return true if the token is valid, false otherwise
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public String extractTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}
		
		Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
			.filter(cookie -> "refreshToken".equals(cookie.getName()))
			.findFirst();
		
		return refreshTokenCookie.map(Cookie::getValue).orElse(null);
	}
}
