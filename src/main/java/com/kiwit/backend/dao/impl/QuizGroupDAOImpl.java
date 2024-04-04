package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.repository.QuizGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizGroupDAOImpl implements QuizGroupDAO {

    private final QuizGroupRepository quizGroupRepository;

    @Autowired
    public QuizGroupDAOImpl(QuizGroupRepository quizGroupRepository) {
        this.quizGroupRepository = quizGroupRepository;
    }

    @Override
    public List<QuizGroup> selectGroupList(Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return quizGroupRepository.findGroupWithCategoryChapter(pageable);
    }

    @Override
    public QuizGroup selectGroupWithQuiz(Long groupId) {
        return quizGroupRepository.findGroupWithQuiz(groupId)
                .orElseThrow(() -> new DataAccessException("Cannot find QuizGroup with groupId.") {});
    }

    @Override
    public QuizGroup selectGroup(Long groupId) {
        return quizGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataAccessException("Cannot find QuizGroup with groupId.") {});
    }


}
