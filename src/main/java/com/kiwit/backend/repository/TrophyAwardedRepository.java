package com.kiwit.backend.repository;

import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.domain.compositeKey.TrophyAwardedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrophyAwardedRepository extends JpaRepository<TrophyAwarded, TrophyAwardedId> {

    @Query("select A " +
            "from TrophyAwarded A " +
            "join fetch A.trophy " +
            "where A.id.userId = :userId " +
            "order by A.updatedAt desc")
    List<TrophyAwarded> findTrophyAwarded(@Param("userId") Long userId);

    @Query("select A " +
            "from TrophyAwarded A " +
            "join fetch A.trophy " +
            "where A.id.userId = :userId " +
            "order by A.updatedAt desc " +
            "limit 1")
    Optional<TrophyAwarded> findTrophyAwardedLatest(@Param("userId") Long userId);
}
