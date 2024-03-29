package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
public class SignUpReqDTO {

    @Email
    private String email;

    @NotBlank
    private String nickname;

    private Provider provider;

}
