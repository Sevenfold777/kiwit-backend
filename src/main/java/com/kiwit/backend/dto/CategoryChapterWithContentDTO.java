package com.kiwit.backend.dto;

import com.kiwit.backend.domain.Category;
import com.kiwit.backend.domain.Content;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CategoryChapterWithContentDTO {

    private Long id;
    private String title;
    private List<ContentDTO> contentList;
}
