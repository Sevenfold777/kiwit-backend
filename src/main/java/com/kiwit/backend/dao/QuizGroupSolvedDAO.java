package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroupSolved;

import java.util.List;

public interface QuizGroupSolvedDAO {

    QuizGroupSolved saveGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved selectGroupLatestSolved(Long userId);

    List<QuizGroupSolved> selectGroupSolved(Long userId, Integer next, Integer limit);

    QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId);
}
