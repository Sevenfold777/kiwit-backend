package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_group_solved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizGroupSolved extends BaseEntity {
    
    @EmbeddedId
    private QuizGroupSolvedId id;

    @Column(nullable = false)
    private Integer highestScore;

    @Column(nullable = false)
    private Integer latestScore;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("quizGroupId")
    @JoinColumn(name = "quiz_group_id")
    private QuizGroup quizGroup;

}
