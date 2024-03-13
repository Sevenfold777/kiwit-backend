package com.kiwit.backend.dto;

import lombok.Builder;

@Builder
public class ContentStudiedDTO {

    private Long userId;
    private Long contentId;
    private Boolean myAnswer;
    private Boolean kept;
}
