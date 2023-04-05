package com.eunes.gateway.service;

import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.eunes.gateway.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Flux<User> getUsers();
    
    Mono<User> getById(UUID id);

    Mono<User> getByUsername(String username);

    Mono<User> create(User user);

    Mono<User> update(User user);

    Mono<Void> delete(UUID id);

    Flux<SimpleGrantedAuthority> getAuthorities(User user);
}
