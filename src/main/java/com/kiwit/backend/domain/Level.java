package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@EqualsAndHashCode(of = "num")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    @Column
    private String title;

//    @OneToMany(mappedBy = "level")
//    private List<Content> contentList = new ArrayList<>();

//    @OneToMany(mappedBy = "level")
//    private List<QuizGroup> quizGroupList = new ArrayList<>();
}
