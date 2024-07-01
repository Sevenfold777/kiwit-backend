package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizKeptDTO {

    private Long userId;
    private Long quizId;
    private QuizSolvedDTO quizSolved;
}
