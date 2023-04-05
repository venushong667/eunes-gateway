package com.eunes.gateway.service;

import java.util.List;
import java.util.UUID;

import com.eunes.gateway.model.Role;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRoleService {
    Flux<Role> getRoles();
    
    Mono<Role> getById(UUID id);

    Flux<Role> getByIds(List<UUID> ids);

    Mono<Role> create(Role role);

    Mono<Role> update(Role role);

    Mono<Void> delete(UUID id);
}
