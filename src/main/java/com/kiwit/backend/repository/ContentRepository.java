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


//    @Query("select C " +
//            "from Content C " +
//            "where C.id > " +
//            "(select P.content.id " +
//            "from Progress P " +
//            "where P.id = :userId) " +
//            "order by C.id asc " +
//            "limit 1")
    @Query("select C " +
            "from Content C " +
            "where C.id > " +
            "(select coalesce((" +
            "select S.id.contentId " +
            "from ContentStudied S " +
            "where S.id.userId = :userId " +
            "order by S.updatedAt desc " +
            "limit 1), 0)) " +
            "order by C.id asc " +
            "limit 1")
    Optional<Content> findNextContent(@Param("userId") Long userId);
}
