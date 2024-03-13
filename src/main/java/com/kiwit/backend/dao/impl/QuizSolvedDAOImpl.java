package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.domain.compositeKey.QuizSolvedId;
import com.kiwit.backend.repository.QuizSolvedRepository;
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
        List<QuizSolved> solvedResult = this.quizSolvedRepository.saveAll(quizSolvedList);
        return solvedResult;
    }

    @Override
    public List<QuizSolved> updateQuizSolvedList(List<QuizSolved> quizSolvedList) {
        List<QuizSolved> solvedResult = this.quizSolvedRepository.saveAll(quizSolvedList);
        return solvedResult;
    }

    @Override
    public QuizSolved keepQuiz(Long userId, Long quizId) {
        Optional<QuizSolved> tgtQuiz = quizSolvedRepository.findById(new QuizSolvedId(userId, quizId));


        QuizSolved updatedQuiz;
        if (tgtQuiz.isPresent()) {
            QuizSolved quiz = tgtQuiz.get();

            quiz.setKept(!tgtQuiz.get().getKept());

            updatedQuiz = quizSolvedRepository.save(quiz);
        } else {
            return  null;
//            throw new Exception();
        }

        return updatedQuiz;
    }

    @Override
    public List<QuizSolved> selectQuizKept(Long userId) {
        List<QuizSolved> quizSolvedList = quizSolvedRepository.findQuizKept(userId);
        return quizSolvedList;
    }

    @Override
    public List<QuizSolved> selectQuizSolved(Long userId) {
        List<QuizSolved> quizSolvedList = quizSolvedRepository.findQuizKept(userId);
        return quizSolvedList;
    }
}