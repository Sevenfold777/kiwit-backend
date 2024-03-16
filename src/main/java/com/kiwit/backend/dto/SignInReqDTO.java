package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.Provider;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SignInReqDTO {

    @NotBlank
    private String token;

    @NotBlank
    private Provider provider;
}
