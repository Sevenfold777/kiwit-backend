package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query("select P " +
            "from Progress P " +
            "where P.id = :userId")
    Optional<Progress> findProgressByUserId(@Param("userId") Long userId);
}
