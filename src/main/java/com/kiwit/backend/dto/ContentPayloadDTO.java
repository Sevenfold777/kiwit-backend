package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentPayloadDTO {
    private Integer number;
    private Long contentId;
    private String type;
    private String payload;
}
