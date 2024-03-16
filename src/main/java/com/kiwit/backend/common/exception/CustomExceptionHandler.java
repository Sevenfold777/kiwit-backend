package com.kiwit.backend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleKnownException(CustomException e, HttpServletRequest request) {
        // HttpHeaders responseHeaders = new HttpHeaders();
        // TODO: Log Exception
        return new ResponseEntity<>(e.getResponseBody(), null, e.getStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Void> handleUnknownException(Exception e, HttpServletRequest request) {
        // HttpHeaders responseHeaders = new HttpHeaders();
        // TODO: Log Exception
        System.out.println(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
