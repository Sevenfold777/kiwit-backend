package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizGroupRepository extends JpaRepository<QuizGroup, Long> {

    @Query("select G " +
            "from QuizGroup G " +
            "join fetch G.categoryChapter " +
            "order by G.id asc")
    List<QuizGroup> findGroupWithCategoryChapter(Pageable pageable);

    @Query("select G " +
            "from QuizGroup G " +
            "join fetch G.quizList " +
            "where G.id = :groupId")
    Optional<QuizGroup> findGroupWithQuiz(@Param("groupId") Long groupId);


}
