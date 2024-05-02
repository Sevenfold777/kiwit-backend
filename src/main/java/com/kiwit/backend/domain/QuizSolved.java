package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "quiz_solved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
//@DynamicInsert // 사용시 batch insert 적용되지 않음
@DynamicUpdate
public class QuizSolved extends BaseEntity implements Persistable<QuizSolvedId> {

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

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
