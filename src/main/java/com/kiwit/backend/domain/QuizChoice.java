package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizChoiceId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_choice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class QuizChoice {

    @EmbeddedId
    private QuizChoiceId id;

    @Column(nullable = false)
    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
