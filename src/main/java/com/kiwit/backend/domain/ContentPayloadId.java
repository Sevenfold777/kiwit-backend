package com.kiwit.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ContentPayloadId implements Serializable {
    @Column(name = "number")
    private Integer number;

    @Column(name = "content_id")
    private Long contentId;
}
