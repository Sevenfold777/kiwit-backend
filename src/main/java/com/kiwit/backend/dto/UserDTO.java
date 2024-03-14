package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String nickname;
    private Integer point;
    private String plan;
    private String status;
}
