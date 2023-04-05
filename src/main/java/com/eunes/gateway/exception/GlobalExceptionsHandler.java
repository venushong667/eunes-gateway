// package com.eunes.gateway.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
// @RestControllerAdvice
// public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler {
    
//     @ExceptionHandler(NotFoundElementException.class)
//     @ResponseStatus(HttpStatus.NOT_FOUND)
//     public ResponseEntity<Object> handleNoSuchElementFoundException(NotFoundElementException exception, WebRequest request) {
//         log.error("Failed to find the requested element.", exception);
//         return new ResponseEntity<>(exception.message, HttpStatus.NOT_FOUND);
//     }
// }
