package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;

import java.util.List;

public interface QuizService {

    List<QuizGroupDTO> getQuizGroup(Integer next, Integer limit);
    QuizGroupWithQuizDTO solveQuizGroup(User user, Long groupId);
    QuizGroupSolvedDTO submitAnswers(User user, Long groupId, QuizAnswerListDTO quizAnswerListDTO);
    QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User user);
    List<QuizGroupWithSolvedDTO> getQuizGroupSolved(User user, Integer next, Integer limit);
    List<QuizWithSolvedDTO> getQuizKept(User user, Integer next, Integer limit);
//    List<QuizWithSolvedDTO> getQuizSolved(User user, Integer next, Integer limit);
    QuizKeptDTO keepQuiz(User user, Long quizId);
}
