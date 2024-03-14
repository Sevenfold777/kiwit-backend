package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.repository.QuizGroupSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizGroupSolvedDAOImpl implements QuizGroupSolvedDAO {

    private final QuizGroupSolvedRepository quizGroupSolvedRepository;

    @Autowired
    public QuizGroupSolvedDAOImpl(QuizGroupSolvedRepository quizGroupSolvedRepository) {
        this.quizGroupSolvedRepository = quizGroupSolvedRepository;
    }

    @Override
    public QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved) {
        QuizGroupSolved savedQuizGroupSolved = quizGroupSolvedRepository.save(groupSolved);
        return savedQuizGroupSolved;
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

    @Override
    public List<QuizGroupSolved> selectGroupSolved(Long userId) {
        List<QuizGroupSolved> quizGroupSolved = quizGroupSolvedRepository.findGroupSolved(userId);
        return quizGroupSolved;
    }

    @Override
    public QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId) {
        Optional<QuizGroupSolved> quizGroupSolved =quizGroupSolvedRepository.findGroupByUserAndGroup(userId, groupId);
        return quizGroupSolved.get();
    }
}
