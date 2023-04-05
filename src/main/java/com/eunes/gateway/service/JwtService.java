package com.eunes.gateway.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eunes.gateway.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtService {
    // private static final long serialVersionUID = -2550185165626007488L;

    // In minutes
	@Value("${jwt.expires}")
	private long JWT_TOKEN_VALIDITY;

	@Value("${jwt.secret}")
	private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

    private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

    //generate token for user
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
		return doGenerateToken(claims, user.getId().toString());
	}

    private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 60 * 1000))
			.signWith(key)
            .compact();
	}

    //validate token
	public Boolean isTokenValid(String token, String username) {
		final String userId = getUserIdFromToken(token);
		return (userId.equals(username) && !isTokenExpired(token));
	}
}
