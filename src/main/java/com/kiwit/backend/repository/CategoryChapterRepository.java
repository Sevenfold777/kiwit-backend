package com.kiwit.backend.repository;

import com.kiwit.backend.domain.CategoryChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryChapterRepository extends JpaRepository<CategoryChapter, Long> {

    @Query("select Ch " +
            "from CategoryChapter Ch " +
            "join fetch Ch.contentList " +
            "where Ch.category.id = :categoryId")
    public List<CategoryChapter> findByCategoryId(@Param("categoryId") Long categoryId);
}
