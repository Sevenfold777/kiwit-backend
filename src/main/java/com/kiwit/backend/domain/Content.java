package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
//@ToString
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_num")
    @ToString.Exclude
    private Level level;

    @OneToMany(mappedBy = "content")
    @PrimaryKeyJoinColumn
    private List<ContentPayload> payloadList = new ArrayList<>();

    @OneToMany(mappedBy = "content")
    @ToString.Exclude
    @PrimaryKeyJoinColumn
    private List<ContentStudied> contentStudiedList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_chapter_id")
    @ToString.Exclude
    private CategoryChapter categoryChapter;

    public Long getLevelNum() {
        if (level != null) {
            return level.getNum();
        } else {
            return  null;
        }
    }

    public Long getCategoryChapterId() {
        if (categoryChapter != null) {
            return categoryChapter.getId();
        } else {
            return  null;
        }
    }
}
