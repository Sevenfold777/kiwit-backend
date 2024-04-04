package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import com.kiwit.backend.repository.QuizSolvedRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizSolvedDAOImpl implements QuizSolvedDAO {

    private final QuizSolvedRepository quizSolvedRepository;

    @Autowired
    public QuizSolvedDAOImpl(QuizSolvedRepository quizSolvedRepository) {
        this.quizSolvedRepository = quizSolvedRepository;
    }

    @Override
    public List<QuizSolved> insertQuizSolvedList(List<QuizSolved> quizSolvedList) {
        return this.quizSolvedRepository.saveAll(quizSolvedList);
    }

    @Override
    public List<QuizSolved> updateQuizSolvedList(List<QuizSolved> quizSolvedList) {
        return this.quizSolvedRepository.saveAll(quizSolvedList);
    }

    @Transactional
    @Override
    public QuizSolved keepQuiz(Long userId, Long quizId) {
        QuizSolved quizSolved = quizSolvedRepository.findById(new QuizSolvedId(userId, quizId))
                .orElseThrow(() -> new DataAccessException("Cannot find Quiz Solved with userId and quizId") {});

        quizSolved.setKept(!quizSolved.getKept());

        return quizSolved;
    }

    @Override
    public List<QuizSolved> selectQuizKept(Long userId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return quizSolvedRepository.findQuizKept(userId, pageable);
    }

    @Override
    public List<QuizSolved> selectQuizSolved(Long userId) {
        return quizSolvedRepository.findQuizSolved(userId);
    }

    @Override
    public List<QuizSolved> selectQuizSolvedWithQuizByGroup(Long userId, Long groupId) {
        return quizSolvedRepository.findQuizSolvedWithQuiz(userId, groupId);
    }
}
