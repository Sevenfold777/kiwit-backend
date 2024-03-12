package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtitle;


    @OneToMany(mappedBy = "group")
    private List<Quiz> quizList = new ArrayList<>();

    @OneToMany(mappedBy = "quizGroup")
    private List<QuizGroupSolved> quizGroupSolvedList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "level_num")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "category_chapter_id")
    private CategoryChapter categoryChapter;
}

