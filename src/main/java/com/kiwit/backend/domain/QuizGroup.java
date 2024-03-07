package com.kiwit.backend.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_group")
public class QuizGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtitle;

    @OneToMany(mappedBy = "quizGroup")
    private List<QuizGroupInvolved> quizGroupInvolvedList = new ArrayList<>();
}

