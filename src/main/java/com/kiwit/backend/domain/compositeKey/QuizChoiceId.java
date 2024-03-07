package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class QuizChoiceId implements Serializable {

    private Integer number;

    @Column(name = "quiz_id")
    private long quizId;
}
