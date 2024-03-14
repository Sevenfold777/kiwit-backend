package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Content;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentStudiedRepository extends JpaRepository<ContentStudied, ContentStudiedId> {

    @Query("select S " +
            "from ContentStudied S " +
            "join fetch S.content " +
            "where S.id.userId = :userId " +
            "and S.kept = true")
    List<ContentStudied> findContentKept(@Param("userId") Long userId);

    @Query("select S " +
            "from ContentStudied S " +
            "join fetch S.content " +
            "where S.id.userId = :userId")
    List<ContentStudied> findContentStudied(@Param("userId") Long userId);
}
