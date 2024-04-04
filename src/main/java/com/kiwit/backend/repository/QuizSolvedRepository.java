package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizSolvedRepository extends JpaRepository<QuizSolved, QuizSolvedId> {

    @Query("select S " +
            "from QuizSolved S " +
            "join fetch S.quiz " +
            "where S.id.userId = :userId " +
            "and S.kept = true " +
            "order by S.updatedAt desc")
    List<QuizSolved> findQuizKept(@Param("userId") Long userId, Pageable pageable);

    @Query("select S " +
            "from QuizSolved S " +
            "join fetch S.quiz " +
            "where S.id.userId = :userId")
    List<QuizSolved> findQuizSolved(@Param("userId") Long userId);


    @Query("select S " +
            "from QuizSolved S " +
            "join fetch S.quiz " +
            "where S.id.userId = :userId " +
            "and S.quiz.group.id = :groupId")
    List<QuizSolved> findQuizSolvedWithQuiz(@Param("userId") Long userId, @Param("groupId") Long groupId);
}
