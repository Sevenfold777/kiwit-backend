package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.domain.QuizSolved;

import java.util.List;

public interface QuizSolvedDAO {
    List<QuizSolved> insertQuizSolvedList(List<QuizSolved> quizSolvedList);

    List<QuizSolved> updateQuizSolvedList(List<QuizSolved> quizSolvedList);

    QuizSolved keepQuiz(Long userId, Long quizId);

    List<QuizSolved> selectQuizKept(Long userId);

    List<QuizSolved> selectQuizSolved(Long userId);
}
