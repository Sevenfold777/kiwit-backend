package com.kiwit.backend.dto;

import lombok.*;


@Getter
@AllArgsConstructor
public class SignInResDTO {
    private String accessToken;
    private String refreshToken;
}
