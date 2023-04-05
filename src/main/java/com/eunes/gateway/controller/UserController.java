package com.eunes.gateway.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eunes.gateway.dto.UpdateUserDto;
import com.eunes.gateway.dto.UserDto;
import com.eunes.gateway.exception.NotFoundElementException;
import com.eunes.gateway.mapper.UserMapper;
import com.eunes.gateway.model.User;
import com.eunes.gateway.service.IUserService;
import com.eunes.gateway.utils.View;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.noContent;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;
    private final UserMapper userMapper;
    
    @GetMapping()
    @JsonView(View.Default.class)
    Flux<User> getUsers() {
        Flux<User> users = userService.getUsers();
        
		return users;
	}

    @GetMapping("/{id}")
	public Mono<User> getUser(@PathVariable UUID id) {
        Mono<User> user = userService.getById(id)
            .switchIfEmpty(Mono.error(new NotFoundElementException("User ID does not exists.")));
        
		return user;
	}

    @PostMapping()
	public Mono<User> createUser(@RequestBody UserDto userDTO) {
        User user = userMapper.toUser(userDTO);
        Mono<User> newUser = userService.create(user);

        return newUser;
	}

    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDto userDto) {
        return userService.getById(id)
                .map(user -> userMapper.update(userDto, user))
                .flatMap(userService::update);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable final UUID id) {
        return userService.delete(id)
                .map(empty -> noContent().build());
    }
}
