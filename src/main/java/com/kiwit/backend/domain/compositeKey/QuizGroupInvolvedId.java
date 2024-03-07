package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class QuizGroupInvolvedId {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "quiz_id")
    private Long quizId;
}
