package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("select C  " +
            "from Content C " +
            "where C.level.num = :levelNum " +
            "order by C.id asc")
    List<Content> findAllByLevelId(@Param("levelNum") Long levelId, Pageable pageable);

    @Query("select C " +
            "from Content C " +
            "where C.id = " +
            "(select S.id.contentId " +
            "from ContentStudied S " +
            "where S.id.userId = :userId " +
            "order by S.updatedAt desc " +
            "limit 1) " +
            "order by C.id asc " +
            "limit 1")
    Optional<Content> findStudiedLatest(@Param("userId") Long userId);

    @Query("select C" +
            " from Content C" +
            " left join C.contentStudiedList S" +
            "   on S.id.userId = :userId" +
            " where C.id = :contentId" +
            " order by C.id" +
            " limit 1")
    Optional<Content> findContentWithStudied(@Param("contentId") Long contentId, @Param("userId") Long userId);
}
