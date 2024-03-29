package com.kiwit.backend.domain.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ContentStudiedId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content_id")
    private Long contentId;


}
