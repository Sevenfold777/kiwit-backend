package com.kiwit.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizGroupSolvedDTO {

    private Long userId;

    private Long quizGroupId;

    private Integer latestScore;

    private Integer highestScore;
}
