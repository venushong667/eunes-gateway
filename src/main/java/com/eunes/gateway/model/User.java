package com.eunes.gateway.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import com.eunes.gateway.utils.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    @JsonView(View.Default.class)
    private UUID id;
    
    @NotEmpty
    @JsonView(View.Default.class)
    private String name;
    
    @NotEmpty
    @JsonView(View.Default.class)
    private String username;
    
    @NotEmpty
    @JsonIgnore
    private String password;
    
    @NotEmpty
    @Email
    @JsonView(View.Default.class)
    private String email;
    
    @Transient
    private Set<Role> roles = new HashSet<>();
}
