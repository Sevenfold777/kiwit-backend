package com.kiwit.backend.common.exception;

import com.kiwit.backend.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleKnownException(CustomException e, HttpServletRequest request) {
        // @AuthenticationPrincipal User authUser + authUser.getId() => can get userId
        if (e.getStatusCode().is2xxSuccessful()) {
            log.info(e.getStatusCode() + " " + e);
        }
        else {
            log.error(e.getStatusCode() + " " + e);
        }

        return new ResponseEntity<>(e.getResponseBody(), null, e.getStatusCode());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(HttpStatus.BAD_REQUEST.toString() + " " + e);
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<Void> handleValidationException(DataAccessException e, HttpServletRequest request) {
        log.error(HttpStatus.BAD_REQUEST.toString() + " " + e);
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Void> handleUnknownException(Exception e, HttpServletRequest request) {
//        e.printStackTrace();
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString() + " " + e);
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Void> handleNoHandlerException(NoHandlerFoundException e) {

        log.error(HttpStatus.NOT_FOUND.toString() + " " + e);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
