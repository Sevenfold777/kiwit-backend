package com.kiwit.backend.dto;

import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.domain.QuizGroupSolved;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuizGroupWithSolvedDTO {

    private Long id;
    private String title;
    private String subtitle;
    private Long levelNum;
    private Integer totalScore;
    private QuizGroupSolvedDTO result;
}
