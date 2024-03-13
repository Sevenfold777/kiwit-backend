package com.kiwit.backend.dto;

import com.kiwit.backend.domain.QuizChoice;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizSolved;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class QuizDTO {
    private Long id;
    private String type;
    private String title;
    private String question;
    private String answer;
    private String explanation;
    private Integer score;
    private List<QuizChoiceDTO> choiceList;
}
