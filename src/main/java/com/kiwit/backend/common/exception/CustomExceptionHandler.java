package com.kiwit.backend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleKnownException(CustomException e, HttpServletRequest request) {
        // HttpHeaders responseHeaders = new HttpHeaders();
        // TODO: Log Exception
        System.out.println(e.toString());
        return new ResponseEntity<>(e.getResponseBody(), null, e.getStatusCode());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        // TODO: Log Exception
        System.out.println(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<Void> handleValidationException(DataAccessException e, HttpServletRequest request) {
        // TODO: Log Exception
        System.out.println("SQL EXCCCCC!: " + e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Void> handleUnknownException(Exception e, HttpServletRequest request) {
        // TODO: Log Exception
        System.out.println(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
