package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.domain.Quiz;
import com.kiwit.backend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizDAOImpl implements QuizDAO {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizDAOImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public List<Quiz> findQuizWithChoiceByGroupId(Long groupId) {
        return quizRepository.findQuizWithChoiceByGroupId(groupId);
    }
}
