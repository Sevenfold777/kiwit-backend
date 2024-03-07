package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz_solved")
public class QuizSolved extends BaseEntity {

    @EmbeddedId
    private QuizSolvedId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
