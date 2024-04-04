package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroupSolved;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuizGroupSolvedDAO {

    QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved selectGroupLatestSolved(Long userId);

    List<QuizGroupSolved> selectGroupSolved(Long userId, Integer next, Integer limit);

    QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId);
}
