package com.eunes.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundElementException extends RuntimeException {

    public String message;

    public NotFoundElementException(String message){
        super(message);

        this.message = message;
    }
}
