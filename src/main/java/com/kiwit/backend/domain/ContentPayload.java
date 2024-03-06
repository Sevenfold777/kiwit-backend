package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_payload")
public class ContentPayload {

    @EmbeddedId
    private ContentPayloadId id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String payload;

    @ManyToOne
    @MapsId("contentId")
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private Content content;

}


