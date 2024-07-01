package com.kiwit.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    max length 설정하기
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "payload_url")
    private String payloadUrl;

    @Column(nullable = false)
    private Integer orderCat;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @Column(nullable = false)
    private String exercise;

    @Column(nullable = false)
    private Boolean answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_num")
    private Level level;

    @OneToMany(mappedBy = "content")
    private List<ContentStudied> contentStudiedList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_chapter_id")
    private CategoryChapter categoryChapter;

    public Content(String title, String payloadUrl, int orderCat, int point, String exercise, boolean answer, Level level, CategoryChapter categoryChapter) {
        this.title = title;
        this.payloadUrl = payloadUrl;
        this.orderCat = orderCat;
        this.point = point;
        this.exercise = exercise;
        this.answer = answer;
        this.level = level;
        this.categoryChapter = categoryChapter;
    }

    public Content(Long id) {
        this.id = id;
    }

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
