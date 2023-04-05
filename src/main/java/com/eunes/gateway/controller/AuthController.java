package com.eunes.gateway.controller;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eunes.gateway.dto.AuthDto;
import com.eunes.gateway.exception.UnauthorizedException;
import com.eunes.gateway.model.JwtToken;
import com.eunes.gateway.model.User;
import com.eunes.gateway.service.IAuthService;
import com.eunes.gateway.service.IUserService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;

    @PostMapping("/login")
    public Mono<JwtToken> login(@RequestBody AuthDto authDto) throws Exception {
        Mono<JwtToken> token = authService.authLogin(authDto.getUsername(), authDto.getPassword());

        return token;
    }
    
    @GetMapping("/profile")
    public Mono<User> getProfile(Authentication authentication) {
        if (authentication == null) {
            return Mono.error(new UnauthorizedException("Invalid authentication."));
        }
        return userService.getById(UUID.fromString(authentication.getPrincipal().toString()));
    }
}
