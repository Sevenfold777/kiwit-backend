package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizKept;

import java.util.HashMap;
import java.util.List;

public interface QuizKeptDAO {

    QuizKept selectQuizKept(Long quizId, Long userId);

    QuizKept insertQuizKept(QuizKept quizKept);

    void deleteQuizKept(QuizKept quizKept);

    List<QuizKept> selectQuizKeptList(Long userId, Integer next, Integer limit);

    HashMap<Long, QuizKept> selectQuizKeptInQuizIdList(Long userId, List<Long> quizIdList);
}
