package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "content_studied")
public class ContentStudied extends BaseEntity {

    @EmbeddedId
    private ContentStudiedId id;

    @Column
    private Boolean myAnswer;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean kept;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("contentId")
    @JoinColumn(name = "content_id")
    private Content content;
}
