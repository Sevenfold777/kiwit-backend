package com.kiwit.backend.repository;

import com.kiwit.backend.domain.QuizGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizGroupRepository extends JpaRepository<QuizGroup, Long> {
}
