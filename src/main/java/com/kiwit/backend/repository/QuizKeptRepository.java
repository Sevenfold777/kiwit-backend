package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizKept;
import com.kiwit.backend.domain.compositeKey.QuizKeptId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizKeptRepository extends JpaRepository<QuizKept, QuizKeptId> {

    @Query("select K " +
            "from QuizKept K " +
            "join fetch K.quiz " +
            "left join fetch K.quizSolved " +
            "where K.id.userId = :userId " +
            "order by K.updatedAt desc")
    List<QuizKept> findQuizKeptList(@Param("userId") Long userId, Pageable pageable);


    @Query("select K" +
            " from QuizKept K" +
            " where K.id.userId = :userId" +
            " and K.id.quizId IN (:quizIdList)")
    List<QuizKept> findQuizKeptInQuizIdList(@Param("userId") Long userId, @Param("quizIdList") List<Long> quizIdList);
}
