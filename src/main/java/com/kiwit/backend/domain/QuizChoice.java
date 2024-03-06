package com.kiwit.backend.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_choice")
public class QuizChoice {

    @Id
    private Integer number;

    @Id
    @Column(name = "quiz_id")
    private long quizId;

    @Column(nullable = false)
    private String payload;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
