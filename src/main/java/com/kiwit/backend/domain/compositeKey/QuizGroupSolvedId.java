package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
public class QuizGroupSolvedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quiz_group_id")
    private Long quizGroupId;
}
