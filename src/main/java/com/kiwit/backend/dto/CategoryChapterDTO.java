package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryChapterDTO {
    private Long id;
    private String title;
}
