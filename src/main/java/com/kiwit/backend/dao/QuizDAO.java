package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Quiz;

import java.util.List;

public interface QuizDAO {

    List<Quiz> selectQuizKept(Long userId, Integer next, Integer limit, Boolean kept);

    List<Quiz> selectQuizSolved(Long userId, Integer next, Integer limit);
}
