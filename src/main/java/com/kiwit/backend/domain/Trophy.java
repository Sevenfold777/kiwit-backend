package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trophy")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Trophy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column
    private String imageUrl;
}