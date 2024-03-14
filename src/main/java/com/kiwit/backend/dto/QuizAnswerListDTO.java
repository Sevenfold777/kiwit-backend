package com.kiwit.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerListDTO {

    @NotEmpty
    private List<QuizAnswerDTO> answerList;
}