package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Quiz;

import java.util.List;

public interface QuizDAO {

    List<Quiz> findQuizWithChoiceByGroupId(Long groupId);
}
