package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSolvedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quiz_id")
    private Long quizId;
}
