package com.eunes.gateway.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String email;
    private List<UUID> roleIds;
}
