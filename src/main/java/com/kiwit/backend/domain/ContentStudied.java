package com.kiwit.backend.domain;

import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "content_studied")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@DynamicInsert
@DynamicUpdate
public class ContentStudied extends BaseEntity implements Persistable<ContentStudiedId> {

    @EmbeddedId
    private ContentStudiedId id;

    @Column
    private Boolean myAnswer;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean kept;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("contentId")
    @JoinColumn(name = "content_id")
    private Content content;

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }

    @Override
    public ContentStudiedId getId() {
        return this.id;
    }
}
