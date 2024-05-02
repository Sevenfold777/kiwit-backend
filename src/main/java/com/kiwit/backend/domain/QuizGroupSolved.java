package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "quiz_group_solved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class QuizGroupSolved extends BaseEntity implements Persistable<QuizGroupSolvedId> {
    
    @EmbeddedId
    private QuizGroupSolvedId id;

    @Column(nullable = false)
    private Integer highestScore;

    @Column(nullable = false)
    private Integer latestScore;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("quizGroupId")
    @JoinColumn(name = "quiz_group_id")
    private QuizGroup quizGroup;

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
