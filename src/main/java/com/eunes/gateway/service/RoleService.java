package com.eunes.gateway.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eunes.gateway.exception.NotFoundElementException;
import com.eunes.gateway.model.Role;
import com.eunes.gateway.repository.RoleRepository;
import com.eunes.gateway.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Flux<Role> getRoles() {
        return roleRepository.findAll();
    }
    
    @Override
    public Mono<Role> getById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Flux<Role> getByIds(List<UUID> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public Mono<Role> create(Role role) {
        role.setName(role.getName().toLowerCase());
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Mono<Role> update(Role role) {
        role.setName(role.getName().toLowerCase());
        Mono<Role> updatedRole = roleRepository.findById(role.getId());
        return updatedRole;
    }

    @Override
    @Transactional
    public Mono<Void> delete(UUID id) {
        return roleRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundElementException("Role id not exists.")))
            .zipWith(userRoleRepository.deleteAllByUserId(id))
            .map(Tuple2::getT1)
            .flatMap(roleRepository::delete);
    }
}
