package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizGroupSolvedRepository extends JpaRepository<QuizGroupSolved, QuizGroupSolvedId> {

    @Query("select S " +
            "from QuizGroupSolved S " +
            "join fetch QuizGroup G " +
            "where S.id.userId = :userId")
    QuizGroupSolved findGroupLatestSolved(@Param("userId") Long userId);
}
