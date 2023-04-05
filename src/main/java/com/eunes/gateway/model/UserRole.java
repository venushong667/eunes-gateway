package com.eunes.gateway.model;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_role")
public class UserRole {

    private UUID userId;

    private UUID roleId;
}
