package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizSolved;

import java.util.List;

public interface QuizSolvedDAO {
    List<QuizSolved> saveQuizSolvedList(List<QuizSolved> quizSolvedList);

    List<QuizSolved> selectQuizSolved(Long userId);

    List<QuizSolved> selectQuizSolvedWithQuizByGroup(Long userId, Long groupId);

    QuizSolved selectQuizSolvedLatest(Long userId, Long quizId);
}
