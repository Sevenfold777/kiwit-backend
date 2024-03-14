package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TrophyAwardedDTO {

    private Long userId;
    private TrophyDTO trophy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
