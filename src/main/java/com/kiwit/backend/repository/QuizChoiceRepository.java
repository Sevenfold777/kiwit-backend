package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizChoice;
import com.kiwit.backend.domain.compositeKey.QuizChoiceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizChoiceRepository extends JpaRepository<QuizChoice, QuizChoiceId> {
}
