package com.kiwit.backend.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

//    @ManyToMany
//    private List<QuizGroup> quizGroupList = new ArrayList<>();
}
