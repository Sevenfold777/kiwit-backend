package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizGroupInvolvedId;
import jakarta.persistence.*;

@Entity
@Table
public class QuizGroupInvolved {

    @EmbeddedId
    private QuizGroupInvolvedId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private QuizGroup quizGroup;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
