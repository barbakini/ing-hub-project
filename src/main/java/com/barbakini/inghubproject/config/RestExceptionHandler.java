package com.barbakini.inghubproject.config;

import com.barbakini.inghubproject.dto.Response;
import com.barbakini.inghubproject.util.exceptions.EntityAlreadyExistException;
import com.barbakini.inghubproject.util.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> notValid(WebExchangeBindException e) {
        List<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        String msg = String.join(", ", errors);
        Response<Object> response = Response.builder()
                .success(false)
                .msg(msg).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<?> entityNotFound(EntityNotFoundException e) {
        Response<Object> response = Response.builder()
                .success(false)
                .msg(e.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    ResponseEntity<?> entityAlreadyExist(EntityAlreadyExistException e) {
        Response<Object> response = Response.builder()
                .success(false)
                .msg(e.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> genericHandler(Exception e) {
        if (e instanceof NoResourceFoundException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Response<Object> response = Response.builder()
                .success(false)
                .msg("Some error occurred during processing your request. System admins were notified.")
                .errMsg(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
