package com.eunes.gateway.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.eunes.gateway.model.Role;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<Role, UUID> {

    Flux<Role> findAll();

    Flux<Role> findAllById(List<UUID> ids);

    Role findByName(String name);

    Mono<Role> findById(UUID id);

    @Query("SELECT r.* FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = :user_id")
    Flux<Role> findAllByUserId(UUID id);

}
