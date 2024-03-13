package com.kiwit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String title;
    private String thumbnailUrl;
}
