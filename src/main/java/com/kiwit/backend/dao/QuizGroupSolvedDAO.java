package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroupSolved;

public interface QuizGroupSolvedDAO {

    QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved updateGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved selectGroupLatestSolved(Long userId);
}
