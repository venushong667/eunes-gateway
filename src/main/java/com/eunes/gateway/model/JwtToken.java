package com.eunes.gateway.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtToken implements Serializable {

    private String accessToken;

    public JwtToken(String accessToken) {
        setAccessToken(accessToken);
    }
    
}
