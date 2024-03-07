package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ContentStudiedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content_id")
    private Long contentId;
}
