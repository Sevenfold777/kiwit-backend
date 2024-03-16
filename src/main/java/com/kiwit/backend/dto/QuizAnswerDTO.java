package com.kiwit.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizAnswerDTO {

    @Positive
    private Long quizId;

    private String answer;
}
