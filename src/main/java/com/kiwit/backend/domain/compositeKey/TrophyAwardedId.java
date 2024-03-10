package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class TrophyAwardedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "trophy_id")
    private Long trophyId;


}
