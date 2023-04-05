package com.eunes.gateway.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eunes.gateway.dto.UpdateUserDto;
import com.eunes.gateway.dto.UserDto;
import com.eunes.gateway.model.Role;
import com.eunes.gateway.model.User;
import com.eunes.gateway.model.UserRole;

import java.util.Collection;
import java.util.LinkedHashSet;

@Component
public class UserMapper {

    @Autowired
    ModelMapper modelMapper;
    // @Autowired
    // RoleService roleService;

    public UserDto toDTO(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        List<UUID> roles = user.getRoles()
          .stream()
          .map(Role::getId)
          .collect(Collectors.toList());
        userDTO.setRoleIds(roles);

        return userDTO;
    }

    public User toUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Set<Role> roles = userDto.getRoleIds().stream()
                    .map(id -> new Role(id, null))
                    .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public User update(UpdateUserDto userDto, User currentUser) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(currentUser.getId());
        user.setPassword(currentUser.getPassword());

        Set<Role> roles = userDto.getRoleIds().stream()
                    .map(id -> new Role(id, null))
                    .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public Collection<UserRole> toUserRole(UUID userId, Collection<Role> roles) {
        if(roles == null) {
            return new LinkedHashSet<>();
        }

        return roles.stream()
                .map(role -> new UserRole(userId, role.getId()))
                .collect(Collectors.toSet());
    }
}