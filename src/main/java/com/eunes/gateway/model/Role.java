package com.eunes.gateway.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("role")
public class Role {
    @Id
    private UUID id;
    
    private String name;

    // @Transient
    // private Set<User> users = new HashSet<>();
}
