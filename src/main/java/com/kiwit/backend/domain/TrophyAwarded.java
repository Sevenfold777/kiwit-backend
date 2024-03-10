package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.TrophyAwardedId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trophy_awarded")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrophyAwarded extends BaseEntity {

    @EmbeddedId
    private TrophyAwardedId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("trophyId")
    @JoinColumn(name = "trophy_id")
    private Trophy trophy;
}
