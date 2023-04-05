package com.eunes.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public String message;

    public UnauthorizedException(String message){
        super(message);

        this.message = message;
    }
}
