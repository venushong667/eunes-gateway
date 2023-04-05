package com.eunes.gateway.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eunes.gateway.exception.NotFoundElementException;
import com.eunes.gateway.model.Role;
import com.eunes.gateway.service.RoleService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.noContent;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    public Flux<Role> getRoles() {
        Flux<Role> roles = roleService.getRoles();

        return roles;
    }

    @GetMapping("/{id}")
	public Mono<Role> getRole(@PathVariable UUID id) {
        Mono<Role> role = roleService.getById(id)
            .switchIfEmpty(Mono.error(new NotFoundElementException("Role ID does not exists.")));
        
		return role;
	}

    @PostMapping()
	public Mono<Role> createRole(@RequestBody Role role) {
        Mono<Role> newRole = roleService.create(role);
        
        return newRole;
	}

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable final UUID id) {
        return roleService.delete(id)
                .map(empty -> noContent().build());
    }
}
