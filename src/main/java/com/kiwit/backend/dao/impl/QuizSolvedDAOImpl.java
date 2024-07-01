package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.repository.QuizSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizSolvedDAOImpl implements QuizSolvedDAO {

    private final QuizSolvedRepository quizSolvedRepository;

    @Autowired
    public QuizSolvedDAOImpl(QuizSolvedRepository quizSolvedRepository) {
        this.quizSolvedRepository = quizSolvedRepository;
    }

    @Override
    public List<QuizSolved> saveQuizSolvedList(List<QuizSolved> quizSolvedList) {
        return this.quizSolvedRepository.saveAll(quizSolvedList);
    }

    @Override
    public List<QuizSolved> selectQuizSolved(Long userId) {
        return quizSolvedRepository.findQuizSolved(userId);
    }

    @Override
    public List<QuizSolved> selectQuizSolvedWithQuizByGroup(Long userId, Long groupId) {
        return quizSolvedRepository.findQuizSolvedWithQuiz(userId, groupId);
    }

    @Override
    public QuizSolved selectQuizSolvedLatest(Long userId, Long quizId) {
        return quizSolvedRepository.findQuizSolvedLatest(userId, quizId)
                .orElse(null);
    }
}
