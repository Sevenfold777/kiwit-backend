package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.Plan;
import com.kiwit.backend.common.constant.Status;
import jakarta.validation.constraints.Email;
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
    private Plan plan;
    private Status status;
}
