package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizChoiceDAO;
import com.kiwit.backend.repository.QuizChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizChoiceDAOImpl implements QuizChoiceDAO {

    private final QuizChoiceRepository quizChoiceRepository;

    @Autowired
    public QuizChoiceDAOImpl(QuizChoiceRepository quizChoiceRepository) {
        this.quizChoiceRepository = quizChoiceRepository;
    }
}
