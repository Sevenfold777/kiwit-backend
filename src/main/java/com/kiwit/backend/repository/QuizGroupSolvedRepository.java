package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizGroupSolvedRepository extends JpaRepository<QuizGroupSolved, QuizGroupSolvedId> {
}
