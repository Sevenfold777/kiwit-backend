package com.kiwit.backend.dto;

import com.kiwit.backend.domain.CategoryChapter;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.Level;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ContentWithStudiedDTO {

    private Long id;
    private String title;
    private Integer point;
    private String exercise;
    private Boolean answer;
    private Long levelNum;
    private Long categoryChapterId;
    private ContentStudiedDTO contentStudied;
}
