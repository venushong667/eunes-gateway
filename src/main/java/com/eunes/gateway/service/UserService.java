package com.eunes.gateway.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eunes.gateway.exception.NotFoundElementException;
import com.eunes.gateway.mapper.UserMapper;
import com.eunes.gateway.model.Role;
import com.eunes.gateway.model.User;
import com.eunes.gateway.repository.RoleRepository;
import com.eunes.gateway.repository.UserRepository;
import com.eunes.gateway.repository.UserRoleRepository;
import com.eunes.gateway.utils.View;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<User> getUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public Mono<User> getById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundElementException("User id not exists.")))
                .flatMap(this::loadRoles);
    }
    
    @Override
    @JsonView(View.Full.class)
    public Mono<User> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoundElementException("Username not exists.")))
                .flatMap(this::loadRoles);
    }

    @Override
    @Transactional
    public Mono<User> create(User user) {
        List<UUID> roleIds = user.getRoles().stream()
            .map(Role::getId)
            .collect(Collectors.toList());

        return this.setNewRoles(user, roleIds).flatMap(newUser -> {
            if (newUser.getRoles().size() != roleIds.size()) {
                return Mono.error(new NotFoundElementException("One or more roles not exists."));
            }

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            return userRepository.save(newUser).flatMap(savedUser -> 
                userRoleRepository.saveAll(userMapper.toUserRole(savedUser.getId(), savedUser.getRoles()))
                    .collectList()
                    .then(Mono.just(savedUser))
            );
        });
    }

    @Override
    @Transactional
    public Mono<User> update(User user) {
        return verifyExistence(user.getId())
            .then(userRoleRepository.findAllByUserId(user.getId()).collectList())
            .flatMap(currentRoles -> {
                List<UUID> roleIds = user.getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());
                
                return this.setNewRoles(user, roleIds).flatMap(updateUser -> {
                    if (updateUser.getRoles().size() != roleIds.size()) {
                        return Mono.error(new NotFoundElementException("One or more roles not exists."));
                    }

                    // final List<UserRole> removedRoles = currentRoles.stream()
                    //     .filter(role -> !roleIds.contains(role.getRoleId()))
                    //     .collect(Collectors.toList());
        
                    updateUser.setPassword(user.getPassword());
                    
                    return userRoleRepository.deleteAllByUserId(user.getId())
                        .thenMany(
                            userRoleRepository.saveAll(userMapper.toUserRole(updateUser.getId(), updateUser.getRoles()))
                        )
                        .then(userRepository.save(updateUser));
                });
            });
    }

    @Override
    @Transactional
    public Mono<Void> delete(UUID id) {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundElementException("User id not exists.")))
            .zipWith(userRoleRepository.deleteAllByUserId(id))
            .map(Tuple2::getT1)
            .flatMap(userRepository::delete);
    }

    @Override
    public Flux<SimpleGrantedAuthority> getAuthorities(User user) {
        
        return this.loadRoles(user).flatMapMany(u -> {
            List<SimpleGrantedAuthority> grantList = u.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
            return Flux.fromIterable(grantList);
        });
    }

    private Mono<User> loadRoles(final User user) {
        Mono<User> mono = Mono.just(user)
            .zipWith(roleRepository.findAllByUserId(user.getId()).collect(Collectors.toSet()))
            .map(result -> {
                User resUser = result.getT1();
                resUser.setRoles(result.getT2());
                return resUser;
            });
        
        return mono;
    }

    private Mono<User> setNewRoles(final User user, List<UUID> roleIds) {
        Mono<Set<Role>> getRoles = roleRepository.findAllById(roleIds).collect(Collectors.toSet());

        Mono<User> mono = Mono.just(user)
            .zipWith(getRoles)
            .map(result -> {
                User resUser = result.getT1();
                resUser.setRoles(result.getT2());
                return resUser;
            });

        return mono;
    }

    private Mono<Boolean> verifyExistence(UUID id) {
        return userRepository.existsById(id).handle((exists, sink) -> {
            if (Boolean.FALSE.equals(exists)) {
                sink.error(new NotFoundElementException("User id not exists."));
            } else {
                sink.next(exists);
            }
        });
    }
}
