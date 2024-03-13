package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroup;

import java.util.List;

public interface QuizGroupDAO {

    List<QuizGroup> selectGroupList();

    QuizGroup selectGroupWithQuiz(Long groupId);

}
