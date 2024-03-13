package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "content_studied")
@Getter
@Setter
@Builder
public class ContentStudied extends BaseEntity {

    @EmbeddedId
    private ContentStudiedId id;

    @Column
    private Boolean myAnswer;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean kept;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contentId")
    @JoinColumn(name = "content_id")
    private Content content;
}
