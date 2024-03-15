package com.kiwit.backend.dto;

import com.kiwit.backend.common.constant.QuizType;
import com.kiwit.backend.domain.QuizSolved;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizWithSolvedDTO {
    private Long id;
    private QuizType type;
    private String title;
    private String question;
    private String answer;
    private String explanation;
    private Integer score;
    private QuizSolvedDTO result;
}
