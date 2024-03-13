package com.kiwit.backend.dto;

import com.kiwit.backend.domain.CategoryChapter;
import com.kiwit.backend.domain.Level;
import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.domain.QuizGroupSolved;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class QuizGroupDTO {

    private Long id;
    private String title;
    private String subtitle;
    private Long levelNum;
    private CategoryChapterDTO categoryChapter;
}
