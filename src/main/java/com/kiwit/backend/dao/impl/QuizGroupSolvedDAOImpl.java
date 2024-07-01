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

import java.util.*;

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
        // temporary application level pagination

//        Pageable pageable = PageRequest.of(next, limit);
        List<QuizGroupSolved> quizGroupSolvedList = quizGroupSolvedRepository.findGroupSolved(userId);

        Set<Long> quizGroupIdList = new HashSet<>();
        List<QuizGroupSolved> trimmedQuizGroupSolvedList = new ArrayList<>();

        for (QuizGroupSolved groupSolved : quizGroupSolvedList) {

            if (!quizGroupIdList.contains(groupSolved.getQuizGroup().getId())) {
                trimmedQuizGroupSolvedList.add(groupSolved);
                quizGroupIdList.add(groupSolved.getQuizGroup().getId());
            }
        }

        int fromIndex = next * limit;
        int toIndex = Math.min((next + 1) * limit, trimmedQuizGroupSolvedList.size());

        return trimmedQuizGroupSolvedList.subList(fromIndex, toIndex);

//        return quizGroupSolvedRepository.findGroupSolved(userId, pageable);
    }

    @Override
    public QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId) {
        return quizGroupSolvedRepository.findGroupByUserAndGroup(userId, groupId)
                .orElse(null); // 아직 풀지 않은 퀴즈 그룹
    }
}
