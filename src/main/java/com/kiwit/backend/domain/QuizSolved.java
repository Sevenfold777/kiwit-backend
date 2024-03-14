package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "quiz_solved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSolved extends BaseEntity {

    @EmbeddedId
    private QuizSolvedId id;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean correct;

    @Column(nullable = false)
    private String myAnswer;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean kept;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
