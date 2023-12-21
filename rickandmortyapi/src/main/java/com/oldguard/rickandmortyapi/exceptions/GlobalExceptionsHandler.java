package com.oldguard.rickandmortyapi.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionObject> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ExceptionObject.getExceptionObject(
                HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date()),
                HttpStatus.NOT_FOUND
        );
    }
     @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionObject> handleEntityExistsException(EntityExistsException ex){
        return new ResponseEntity<>(ExceptionObject.getExceptionObject(
                HttpStatus.CONFLICT.value(), ex.getMessage(), new Date()),
                HttpStatus.CONFLICT
        );
    }
}
