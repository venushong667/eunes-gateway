package com.eunes.gateway.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.eunes.gateway.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    Flux<User> findAll();

    Mono<User> findByUsername(String name);

    Mono<User> findById(UUID id);
    
}
