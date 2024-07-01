package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuizGroupSolvedRepository extends JpaRepository<QuizGroupSolved, Long> {

    @Query("select S " +
            "from QuizGroupSolved S " +
            "join fetch S.quizGroup " +
            "where S.user.id = :userId " +
            "order by S.id desc " +
            "limit 1")
    Optional<QuizGroupSolved> findGroupLatestSolved(@Param("userId") Long userId);

//    @Query("select S " +
//            "from QuizGroupSolved S " +
//            "join fetch S.quizGroup " +
//            "where S.user.id = :userId " +
//            "order by S.updatedAt desc")
//List<QuizGroupSolved> findGroupSolved(@Param("userId") Long userId, Pageable pageable);
    @Query("select S " +
            "from QuizGroupSolved S " +
            "join fetch S.quizGroup " +
            "where S.user.id = :userId " +
            "order by S.updatedAt desc")
    List<QuizGroupSolved> findGroupSolved(@Param("userId") Long userId);


    @Query("select S " +
            "from QuizGroupSolved S " +
            "join fetch S.quizGroup " +
            "where S.user.id = :userId " +
            "and S.quizGroup.id = :groupId " +
            "order by S.id desc " +
            "limit 1")
    Optional<QuizGroupSolved> findGroupByUserAndGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);
}

