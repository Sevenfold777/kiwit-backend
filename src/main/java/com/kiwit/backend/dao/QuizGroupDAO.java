package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroup;

import java.util.List;

public interface QuizGroupDAO {

    List<QuizGroup> selectGroupList(Integer next, Integer limit);

    QuizGroup selectGroupWithQuiz(Long groupId);

    QuizGroup selectGroup(Long groupId);
}
