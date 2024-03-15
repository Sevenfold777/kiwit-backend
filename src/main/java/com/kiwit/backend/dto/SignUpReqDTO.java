package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpReqDTO {

    @Email
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private Provider provider;

}
