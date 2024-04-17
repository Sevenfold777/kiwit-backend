package com.kiwit.backend.domain;

import com.kiwit.backend.common.constant.QuizType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private Integer score;

    @OneToMany(mappedBy = "quiz")
    private List<QuizChoice> choiceList = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    private List<QuizSolved> quizSolvedList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private QuizGroup group;

    public Quiz(Long id) {
        this.id = id;
    }
}
