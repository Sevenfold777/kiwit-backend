package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizDAOImpl implements QuizDAO {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizDAOImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }
}
