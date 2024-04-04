package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("select Q " +
            "from Quiz Q " +
            "left join fetch Q.choiceList " +
            "where Q.group.id = :groupId")
    List<Quiz> findQuizWithChoiceByGroupId(Long groupId);
}
