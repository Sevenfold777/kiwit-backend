package com.kiwit.backend.service;

import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizService {

    List<QuizGroupDTO> getQuizGroup();
    QuizGroupWithQuizDTO solveQuizGroup(Long groupId);
    QuizGroupSolvedDTO submitAnswers(Long userId, Long quizId, QuizAnswerListDTO quizAnswerListDTO);
    QuizGroupSolvedDTO resubmitAnswers(Long userId, Long quizId, QuizAnswerListDTO quizAnswerListDTO);
    QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser);
    List<QuizWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit);
    List<QuizWithSolvedDTO> getQuizSolved(User authUser, Integer next, Integer limit);
    QuizSolvedDTO keepQuiz(User authUser, Long quizId);
}
