package com.kiwit.backend.dao;

import com.kiwit.backend.domain.QuizGroupSolved;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuizGroupSolvedDAO {

    QuizGroupSolved insertGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved updateGroupSolved(QuizGroupSolved groupSolved);

    QuizGroupSolved selectGroupLatestSolved(Long userId);

    List<QuizGroupSolved> selectGroupSolved(Long userId);

    QuizGroupSolved selectGroupSolvedWithGroup(Long userId, Long groupId);
}
