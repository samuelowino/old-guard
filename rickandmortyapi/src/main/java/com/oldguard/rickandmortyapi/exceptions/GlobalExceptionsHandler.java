package com.oldguard.rickandmortyapi.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionObject> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ExceptionObject.singleMessageException(
                HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date()),
                HttpStatus.NOT_FOUND
        );
    }
     @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionObject> handleEntityExistsException(EntityExistsException ex){

        return new ResponseEntity<>(ExceptionObject.singleMessageException(
                HttpStatus.CONFLICT.value(), ex.getMessage(), new Date()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
               e-> errors.put(e.getField(),e.getDefaultMessage())
                );

        return new ResponseEntity<>(ExceptionObject.manyMessagesException(
                HttpStatus.BAD_REQUEST.value(), errors, new Date()),
                HttpStatus.BAD_REQUEST
        );
    }
}
