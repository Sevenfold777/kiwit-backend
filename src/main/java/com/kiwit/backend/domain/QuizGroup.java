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

    @Column(nullable = false)
    private Integer totalScore;

    @OneToMany(mappedBy = "group")
    private List<Quiz> quizList = new ArrayList<>();

    @OneToMany(mappedBy = "quizGroup")
    private List<QuizGroupSolved> quizGroupSolvedList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_num")
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_chapter_id")
    private CategoryChapter categoryChapter;

    public Long getLevelNum() {
        if (level != null) {
            return level.getNum();
        } else {
            return null;
        }
    }

    public QuizGroup(Long id) {
        this.id = id;
    }
}

