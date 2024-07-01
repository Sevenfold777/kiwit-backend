package com.kiwit.backend.repository;

import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentStudiedRepository extends JpaRepository<ContentStudied, ContentStudiedId> {

    @Query("select S " +
            "from ContentStudied S " +
            "join fetch S.content " +
            "where S.id.userId = :userId " +
            "and S.kept = true " +
            "order by S.updatedAt desc")
    List<ContentStudied> findContentKept(@Param("userId") Long userId, Pageable pageable);

    @Query("select S " +
            "from ContentStudied S " +
            "join fetch S.content " +
            "where S.id.userId = :userId " +
            "order by S.updatedAt desc")
    List<ContentStudied> findContentStudiedList(@Param("userId") Long userId, Pageable pageable);
}
