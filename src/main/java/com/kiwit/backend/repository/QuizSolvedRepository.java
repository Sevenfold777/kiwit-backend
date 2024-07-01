package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizSolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizSolvedRepository extends JpaRepository<QuizSolved, Long> {

    @Query("select S " +
            "from QuizSolved S " +
            "join fetch S.quiz " +
            "where S.user.id = :userId " +
            "order by S.id asc")
    List<QuizSolved> findQuizSolved(@Param("userId") Long userId);


    @Query("select S " +
            "from QuizSolved S " +
            "join fetch S.quiz " +
            "where S.user.id = :userId " +
            "and S.quiz.group.id = :groupId " +
            "order by S.id asc")
    List<QuizSolved> findQuizSolvedWithQuiz(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query("select S" +
            " from QuizSolved S" +
            " where S.user.id = :userId" +
            " and S.quiz.id = :quizId" +
            " order by S.createdAt desc" +
            " limit 1")
    Optional<QuizSolved> findQuizSolvedLatest(@Param("userId") Long userId, @Param("quizId") Long quizId);
}
