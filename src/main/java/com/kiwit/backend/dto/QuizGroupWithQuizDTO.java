package com.kiwit.backend.dto;

import com.kiwit.backend.domain.Quiz;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuizGroupWithQuizDTO {

    private Long id;
    private String title;
    private String subtitle;

    private List<QuizDTO> quizList;
}
