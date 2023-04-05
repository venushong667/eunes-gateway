package com.eunes.gateway.security;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eunes.gateway.service.IUserService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import static com.eunes.gateway.utils.Constants.rootId;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final IUserService userService;

    @Override
    @Transactional
    public Mono<Authentication> authenticate(Authentication authentication) {
        String userId = authentication.getName();
        // String password = authentication.getCredentials();

        if (userId == rootId) {
            return Mono.just(new UsernamePasswordAuthenticationToken(userId, null, Arrays.asList(new SimpleGrantedAuthority("admin"))));
        }
        
        return userService.getById(UUID.fromString(userId))
            .flatMap(user -> userService.getAuthorities(user).collectList())
            .flatMap(auth -> {
                return Mono.just(new UsernamePasswordAuthenticationToken(userId, null, auth));
            }
        );
    }

}
