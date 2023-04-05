package com.eunes.gateway.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.eunes.gateway.model.UserRole;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long>{

    Flux<UserRole> findAllByUserId(UUID userId);

    Flux<UserRole> findAllByRoleId(UUID roleId);

    Mono<Integer> deleteAllByUserId(UUID id);

    Mono<Integer> deleteAllByRoleId(UUID id);

    
}
