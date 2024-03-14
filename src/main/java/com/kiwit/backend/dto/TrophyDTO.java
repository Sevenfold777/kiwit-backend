package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrophyDTO {
    private Long id;
    private String title;
    private String imageUrl;
}
