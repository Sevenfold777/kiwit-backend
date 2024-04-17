package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuizGroupSolvedId implements Serializable {

//    @Column(name = "user_id") // MapsId로 매핑 됨
    private Long userId;

//    @Column(name = "quiz_group_id")
    private Long quizGroupId;
}
