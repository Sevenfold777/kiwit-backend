package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizKeptId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "quiz_kept")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class QuizKept extends BaseEntity implements Persistable<QuizKeptId> {

    @EmbeddedId
    private QuizKeptId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_solved_id", referencedColumnName = "id")
    private QuizSolved quizSolved;

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
