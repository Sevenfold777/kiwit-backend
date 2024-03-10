package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trophy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trophy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "trophy")
    private List<TrophyAwarded> trophyAwardedList = new ArrayList<>();
}
