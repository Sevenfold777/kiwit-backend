package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizChoiceDTO {

    private Integer number;
    private long quizId;
    private String payload;
}
