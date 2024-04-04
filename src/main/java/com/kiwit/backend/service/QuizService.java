package com.kiwit.backend.service;

import com.kiwit.backend.domain.QuizGroupSolved;
import com.kiwit.backend.domain.QuizSolved;
import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizService {

    List<QuizGroupDTO> getQuizGroup(Integer next, Integer limit);
    QuizGroupWithQuizDTO solveQuizGroup(Long groupId);
    QuizGroupSolvedDTO submitAnswers(User user, Long quizId, QuizAnswerListDTO quizAnswerListDTO);
    QuizGroupSolvedDTO resubmitAnswers(User user, Long quizId, QuizAnswerListDTO quizAnswerListDTO);
    QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User user);
    List<QuizGroupWithSolvedDTO> getQuizGroupSolved(User user, Integer next, Integer limit);
    List<QuizWithSolvedDTO> getQuizKept(User user, Integer next, Integer limit);
//    List<QuizWithSolvedDTO> getQuizSolved(User user, Integer next, Integer limit);
    QuizSolvedDTO keepQuiz(User user, Long quizId);
}
