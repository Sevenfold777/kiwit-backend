package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.QuizGroupSolvedDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.repository.QuizGroupSolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved) {
        try {
            return quizGroupSolvedRepository.save(groupSolved);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public QuizGroupSolved updateGroupSolved(QuizGroupSolved groupSolved) {
        return null;
    }

    @Override
    public QuizGroupSolved selectGroupLatestSolved(Long userId) {
        try {
            return quizGroupSolvedRepository.findGroupLatestSolved(userId);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }


    }   @Override
    public List<QuizGroupSolved> selectGroupSolved(Long userId) {
        try {
            return quizGroupSolvedRepository.findGroupSolved(userId);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }


    }   @Override
    public QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId) {
        Optional<QuizGroupSolved> quizGroupSolved =quizGroupSolvedRepository.findGroupByUserAndGroup(userId, groupId);
        if (quizGroupSolved.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        return quizGroupSolved.get();
    }
}
