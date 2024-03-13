package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.repository.QuizGroupSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizGroupSolvedDAOImpl implements QuizGroupSolvedDAO {

    private final QuizGroupSolvedRepository quizGroupSolvedRepository;

    @Autowired
    public QuizGroupSolvedDAOImpl(QuizGroupSolvedRepository quizGroupSolvedRepository) {
        this.quizGroupSolvedRepository = quizGroupSolvedRepository;
    }

    @Override
    public QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved) {
        return null;
    }

    @Override
    public QuizGroupSolved updateGroupSolved(QuizGroupSolved groupSolved) {
        return null;
    }

    @Override
    public QuizGroupSolved selectGroupLatestSolved(Long userId) {
        QuizGroupSolved quizGroupSolved = quizGroupSolvedRepository.findGroupLatestSolved(userId);
        return quizGroupSolved;
    }
}
