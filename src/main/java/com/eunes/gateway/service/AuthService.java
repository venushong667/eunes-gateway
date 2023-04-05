package com.eunes.gateway.service;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eunes.gateway.model.JwtToken;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {
    
    private final IUserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public Mono<JwtToken> authLogin(String username, String password) {
        return userService.getByUsername(username).flatMap(user -> {
            Boolean valid = passwordEncoder.matches(password, user.getPassword());
            if (!valid) {
                return Mono.error(new AuthenticationException("Password not match."));
            }
            String token = jwtService.generateToken(user);
            JwtToken jwt = JwtToken.builder()
                            .accessToken(token)
                            .build();

            return Mono.just(jwt);
        });
    }
}
