package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentPayloadDTO {
    private Integer number;
    private Long contentId;
    private ContentType type;
    private String payload;
}
