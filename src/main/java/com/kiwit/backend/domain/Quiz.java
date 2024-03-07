package com.kiwit.backend.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @OneToMany(mappedBy = "quiz")
    private List<QuizChoice> choiceList = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    @PrimaryKeyJoinColumn
    private List<QuizSolved> quizSolvedList = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    private List<QuizGroupInvolved> quizGroupInvolvedList = new ArrayList<>();
}
