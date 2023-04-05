package com.eunes.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.eunes.gateway.service.JwtService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    // private final XForwardFilter xForwardFilter;
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange()
            .pathMatchers("/login").permitAll()
            .pathMatchers("/profile").authenticated();

        http
            // .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authenticationManager(authenticationManager)
            .csrf().disable()
            .authorizeExchange()
            .anyExchange().hasAuthority("admin");

        http.addFilterAt(new XForwardFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION);
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager, jwtService), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
