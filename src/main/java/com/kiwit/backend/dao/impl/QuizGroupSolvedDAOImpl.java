package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.repository.QuizGroupSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public QuizGroupSolved saveGroupSolved(QuizGroupSolved groupSolved) {
        return quizGroupSolvedRepository.save(groupSolved);
    }

    @Override
    public QuizGroupSolved selectGroupLatestSolved(Long userId) {
        return quizGroupSolvedRepository.findGroupLatestSolved(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NO_CONTENT));
    }

    @Override
    public List<QuizGroupSolved> selectGroupSolved(Long userId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return quizGroupSolvedRepository.findGroupSolved(userId, pageable);
    }

    @Override
    public QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId) {
        return quizGroupSolvedRepository.findGroupByUserAndGroup(userId, groupId)
                .orElseThrow(() -> new DataAccessException("Cannot find QuizGroupSolved with userId and groupId.") {});
    }
}
