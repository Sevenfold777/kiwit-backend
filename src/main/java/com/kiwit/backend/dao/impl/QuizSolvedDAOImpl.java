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
        Optional<QuizSolved> tgtQuiz = quizSolvedRepository.findById(new QuizSolvedId(userId, quizId));

        QuizSolved quizSolved = tgtQuiz.get();
        quizSolved.setKept(!tgtQuiz.get().getKept());

        return quizSolved;
    }

    @Override
    public List<QuizSolved> selectQuizKept(Long userId) {
        List<QuizSolved> quizSolvedList = quizSolvedRepository.findQuizKept(userId);
        return quizSolvedList;
    }

    @Override
    public List<QuizSolved> selectQuizSolved(Long userId) {
        List<QuizSolved> quizSolvedList = quizSolvedRepository.findQuizSolved(userId);
        return quizSolvedList;
    }

    @Override
    public List<QuizSolved> selectQuizSolvedWithQuizByGroup(Long userId, Long groupId) {
        List<QuizSolved> quizSolvedList = quizSolvedRepository.findQuizSolvedWithQuiz(userId, groupId);
        return quizSolvedList;
    }
}
