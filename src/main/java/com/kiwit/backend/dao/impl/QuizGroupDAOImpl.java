package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.QuizDAO;
import com.kiwit.backend.dao.QuizGroupDAO;
import com.kiwit.backend.domain.QuizGroup;
import com.kiwit.backend.repository.QuizGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<QuizGroup> quizGroupList = quizGroupRepository.findGroupWithCategoryChapter();
        return quizGroupList;
    }

    @Override
    public QuizGroup selectGroupWithQuiz(Long groupId) {
        Optional<QuizGroup> quizGroup = quizGroupRepository.findGroupWithQuiz(groupId);
        return quizGroup.get();
    }

    @Override
    public QuizGroup selectGroup(Long groupId) {
        Optional<QuizGroup> quizGroup = quizGroupRepository.findById(groupId);
        return quizGroup.get();
    }


}
