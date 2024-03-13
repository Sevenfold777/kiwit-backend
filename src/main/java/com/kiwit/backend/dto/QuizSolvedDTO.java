package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSolvedDTO {

    private Long userId;
    private Long quizId;
    private Boolean correct;
    private String myAnswer;
    private Boolean kept;
}
