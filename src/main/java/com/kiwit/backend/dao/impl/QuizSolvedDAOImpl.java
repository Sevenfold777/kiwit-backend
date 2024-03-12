package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizSolvedDAO;
import com.kiwit.backend.repository.QuizSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizSolvedDAOImpl implements QuizSolvedDAO {

    private final QuizSolvedRepository quizSolvedRepository;

    @Autowired
    public QuizSolvedDAOImpl(QuizSolvedRepository quizSolvedRepository) {
        this.quizSolvedRepository = quizSolvedRepository;
    }
}
