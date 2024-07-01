package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizKeptDAO;
import com.kiwit.backend.domain.QuizKept;
import com.kiwit.backend.domain.compositeKey.QuizKeptId;
import com.kiwit.backend.repository.QuizKeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class QuizKeptDAOImpl implements QuizKeptDAO {

    private final QuizKeptRepository quizKeptRepository;

    @Autowired()
    public QuizKeptDAOImpl(QuizKeptRepository quizKeptRepository) {
        this.quizKeptRepository = quizKeptRepository;
    }

    @Override
    public QuizKept selectQuizKept(Long quizId, Long userId) {
        return this.quizKeptRepository.findById(new QuizKeptId(userId, quizId))
                .orElse(null);
    }

    @Override
    public QuizKept insertQuizKept(QuizKept quizKept) {
        return quizKeptRepository.save(quizKept);
    }

    @Override
    public void deleteQuizKept(QuizKept quizKept) {
        quizKeptRepository.delete(quizKept);
    }

    @Override
    public List<QuizKept> selectQuizKeptList(Long userId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return quizKeptRepository.findQuizKeptList(userId, pageable);
    }

    @Override
    public HashMap<Long, QuizKept> selectQuizKeptInQuizIdList(Long userId, List<Long> quizIdList) {
        List<QuizKept> quizKeptList = quizKeptRepository.findQuizKeptInQuizIdList(userId, quizIdList);

        // { quizId : QuizKept } 형태 => Service 에서 사용시 O(1) 빠른 탐색
        HashMap<Long, QuizKept> result = new HashMap<>();

        for (QuizKept quizKept : quizKeptList) {
            assert quizKept.getId() != null;
            result.put(quizKept.getId().getQuizId(), quizKept);
        }

        return result;
    }

}
