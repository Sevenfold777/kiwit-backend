package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "level_chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@EqualsAndHashCode(of = "id")
public class LevelChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    unique = true? if needed (might be not necessary)
    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "levelChapter")
    private List<Content> contentList = new ArrayList<>();

}
