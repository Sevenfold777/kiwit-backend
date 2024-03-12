package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.QuizGroupAnswersDTO;
import com.kiwit.backend.dto.QuizGroupDTO;
import com.kiwit.backend.dto.QuizGroupWithQuizDTO;
import com.kiwit.backend.dto.QuizGroupWithSolvedDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizService {

    List<QuizGroupDTO> getQuizGroup();
    List<QuizGroupWithQuizDTO> solveQuizGroup(Long groupId);
    QuizGroupWithSolvedDTO submitAnswers(Long quizId, QuizGroupAnswersDTO quizGroupAnswersDTO);
    QuizGroupWithSolvedDTO resubmitAnswers(Long quizId, QuizGroupAnswersDTO quizGroupAnswersDTO);
    QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser);
    List<QuizGroupWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit);
    List<QuizGroupWithSolvedDTO> getQuizSolved(User authUser, Integer next, Integer limit);
}
