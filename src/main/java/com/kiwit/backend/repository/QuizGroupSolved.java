package com.kiwit.backend.repository;

import com.kiwit.backend.domain.compositeKey.QuizGroupSolvedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizGroupSolved extends JpaRepository<QuizGroupSolved, QuizGroupSolvedId> {
}
