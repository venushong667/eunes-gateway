package com.eunes.gateway.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.eunes.gateway.exception.UnauthorizedException;
import com.eunes.gateway.service.JwtService;

import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final List<String> requestTokenHeader = exchange.getRequest().getHeaders().get("Authorization");
        Boolean isBearerPrefix = requestTokenHeader != null && requestTokenHeader.get(0).startsWith("Bearer ");
        String jwtToken = isBearerPrefix ? requestTokenHeader.get(0).substring(7) : null;
        
        return ReactiveSecurityContextHolder.getContext()
            .map(Optional::of)
            .defaultIfEmpty(Optional.empty())
            .flatMap(ctx -> {
                if (jwtToken != null && ctx.isEmpty()) {
                    return authJwt(jwtToken).flatMap(auth -> {
                        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                    });
                }
                
                return chain.filter(exchange);
            });
    }

    private Mono<Authentication> authJwt(String jwtToken) {
        String userId = null;

        try {
            userId = jwtService.getUserIdFromToken(jwtToken);
        } catch (Exception e) {
            return Mono.error(new UnauthorizedException(e.getMessage()));
        }
        
        if (userId != null && jwtService.isTokenValid(jwtToken, userId)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null);

            return this.authenticationManager.authenticate(authToken);
        }
        return Mono.error(new UnauthorizedException("Invalid User"));
    }
    
}
