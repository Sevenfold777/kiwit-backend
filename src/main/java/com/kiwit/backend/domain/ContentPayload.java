package com.kiwit.backend.domain;

import com.kiwit.backend.common.constant.ContentType;
import com.kiwit.backend.domain.compositeKey.ContentPayloadId;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "content_payload")
public class ContentPayload {

    @EmbeddedId
    private ContentPayloadId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType type;

    @Column(nullable = false)
    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contentId")
    @ToString.Exclude
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private Content content;

}


