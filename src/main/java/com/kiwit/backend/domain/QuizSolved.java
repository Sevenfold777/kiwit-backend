package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "quiz_solved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSolved extends BaseEntity {

    @EmbeddedId
    private QuizSolvedId id;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean correct;

    @Column(nullable = false)
    private String my_answer;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean kept;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
