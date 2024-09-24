package com.sparta.orderking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handlerEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(com.sparta.orderking.exception.EntityNotFoundException.class)
    public ResponseEntity<String> handlerEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(com.sparta.orderking.exception.WrongConditionException.class)
    public ResponseEntity<String> handlerWrongConditionException(WrongConditionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handlerUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
