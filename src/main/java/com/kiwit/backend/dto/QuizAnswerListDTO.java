package com.kiwit.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuizAnswerListDTO {

    @NotEmpty
    private List<QuizAnswerDTO> answerList;
}