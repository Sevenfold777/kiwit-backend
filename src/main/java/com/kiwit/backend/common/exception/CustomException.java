package com.kiwit.backend.common.exception;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomException extends RuntimeException {

    private HttpStatus statusCode;
    private Map<String, String> responseBody;


    public CustomException(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public CustomException(HttpStatus statusCode, Map<String, String> responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
}
