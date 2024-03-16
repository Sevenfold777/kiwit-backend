package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.repository.QuizGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizGroupDAOImpl implements QuizGroupDAO {

    private final QuizGroupRepository quizGroupRepository;

    @Autowired
    public QuizGroupDAOImpl(QuizGroupRepository quizGroupRepository) {
        this.quizGroupRepository = quizGroupRepository;
    }

    @Override
    public List<QuizGroup> selectGroupList() {
        return quizGroupRepository.findGroupWithCategoryChapter();
    }

    @Override
    public QuizGroup selectGroupWithQuiz(Long groupId) {
        Optional<QuizGroup> quizGroup = quizGroupRepository.findGroupWithQuiz(groupId);
        if (quizGroup.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        return quizGroup.get();
    }

    @Override
    public QuizGroup selectGroup(Long groupId) {
        Optional<QuizGroup> quizGroup = quizGroupRepository.findById(groupId);
        if (quizGroup.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        return quizGroup.get();
    }


}
