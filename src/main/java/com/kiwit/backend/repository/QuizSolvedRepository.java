package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSolvedRepository extends JpaRepository<QuizSolved, QuizSolvedId> {
}
