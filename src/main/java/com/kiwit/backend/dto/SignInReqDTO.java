package com.kiwit.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SignInReqDTO {

    @NotBlank
    private String token;

    // TODO: enumerate
    @NotBlank
    private String provider;
}
