package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrophyAwardedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "trophy_id")
    private Long trophyId;
}
