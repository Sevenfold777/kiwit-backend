package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("select C  " +
            "from Content C " +
            "where C.level.num = :levelNum")
    List<Content> findAllByLevelId(@Param("levelNum") Long levelId);


    @Query("select C " +
            "from Content C " +
            "join fetch C.payloadList " +
            "where C.id = :contentId")
    Optional<Content> findContentWithPayload(@Param("contentId") Long contentId);

    @Query("select C " +
            "from Content C " +
            "join fetch C.contentStudiedList " +
            "where element(C.contentStudiedList).id.userId = :userId " +
            "and element(C.contentStudiedList).kept = true")
    List<Content> findContentKept(@Param("userId") Long userId);

    @Query("select C " +
            "from Content C " +
            "join C.contentStudiedList " +
            "where element(C.contentStudiedList).id.userId = :userId")
    List<Content> findContentStudied(@Param("userId") Long userId);

    @Query("select C " +
            "from Content C")
    Content findByProgressUserId(@Param("userId") Long userId);
}
