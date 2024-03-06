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
    private long id;

//    max length 설정하기
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

//    auto-incrementing order_value would be better (check costs)
    @Column(nullable = false)
    private Integer order_lv;

    @Column(nullable = false)
    private Integer order_cat;

    @ManyToOne
    @JoinColumn(name = "level_chapter_id")
    private LevelChapter levelChapter;

    @OneToMany(mappedBy = "content")
    @PrimaryKeyJoinColumn
    private List<ContentPayload> payloadList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_chapter_id")
    private CategoryChapter categoryChapter;
}
