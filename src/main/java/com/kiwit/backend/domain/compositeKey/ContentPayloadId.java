package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class ContentPayloadId implements Serializable {
    @Column(name = "number")
    private Integer number;

    @Column(name = "content_id")
    private Long contentId;
}
