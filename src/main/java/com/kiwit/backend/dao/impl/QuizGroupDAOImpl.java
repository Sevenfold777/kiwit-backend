package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.repository.QuizGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizGroupDAOImpl implements QuizGroupDAO {

    private final QuizGroupRepository quizGroupRepository;

    @Autowired
    public QuizGroupDAOImpl(QuizGroupRepository quizGroupRepository) {
        this.quizGroupRepository = quizGroupRepository;
    }
}
