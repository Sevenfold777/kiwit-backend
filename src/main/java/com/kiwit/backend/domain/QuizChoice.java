package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizChoiceId;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz_choice")
public class QuizChoice {

    @EmbeddedId
    private QuizChoiceId id;

    @Column(nullable = false)
    private String payload;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
