package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizSolvedRepository extends JpaRepository<QuizSolved, QuizSolvedId> {

    @Query("select S " +
            "from QuizSolved S " +
            "join Quiz Q " +
            "where S.id.userId = :userId " +
            "and S.kept = true")
    List<QuizSolved> findQuizKept(Long userId);

    @Query("select S " +
            "from QuizSolved S " +
            "join Quiz Q " +
            "where S.id.userId = :userId")
    List<QuizSolved> findQuizSolved(Long userId);
}
