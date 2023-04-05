package com.eunes.gateway.service;

import com.eunes.gateway.model.JwtToken;

import reactor.core.publisher.Mono;

public interface IAuthService {
    Mono<JwtToken> authLogin(String username, String password);
}
