package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    max length 설정하기
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @Column(nullable = false)
    private String exercise;

    @Column(nullable = false)
    private Boolean answer;

    @ManyToOne
    @JoinColumn(name = "level_num")
    private Level level;

    @OneToMany(mappedBy = "content")
    @PrimaryKeyJoinColumn
    private List<ContentPayload> payloadList = new ArrayList<>();

    @OneToMany(mappedBy = "content")
    @PrimaryKeyJoinColumn
    private List<ContentStudied> contentStudiedList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_chapter_id")
    private CategoryChapter categoryChapter;

    @OneToMany(mappedBy = "content")
    private List<Progress> progressList = new ArrayList<>();
}
