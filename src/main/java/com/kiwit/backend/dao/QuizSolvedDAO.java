package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizSolved;

import java.util.List;

public interface QuizSolvedDAO {
    List<QuizSolved> saveQuizSolvedList(List<QuizSolved> quizSolvedList);

    QuizSolved keepQuiz(Long userId, Long quizId);

    List<QuizSolved> selectQuizKept(Long userId, Integer next, Integer limit);

    List<QuizSolved> selectQuizSolved(Long userId);

    List<QuizSolved> selectQuizSolvedWithQuizByGroup(Long userId, Long groupId);
}
