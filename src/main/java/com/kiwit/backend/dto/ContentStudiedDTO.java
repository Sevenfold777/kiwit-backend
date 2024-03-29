package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContentStudiedDTO {

    private Long userId;
    private Long contentId;
    private Boolean myAnswer;
    private Boolean kept;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
