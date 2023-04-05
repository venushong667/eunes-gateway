package com.eunes.gateway.security;

import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import reactor.core.publisher.Mono;

import static com.eunes.gateway.utils.Constants.rootId;

public class XForwardFilter extends AuthenticationWebFilter {

    private final ReactiveAuthenticationManager authenticationManager;

    public XForwardFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        List<String> xforward = exchange.getRequest().getHeaders().get("x-forwarded");

        if (xforward != null) {
            return chain.filter(exchange);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(rootId, null);
        return this.authenticationManager.authenticate(authToken).flatMap(auth -> {
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        });
    }
}
