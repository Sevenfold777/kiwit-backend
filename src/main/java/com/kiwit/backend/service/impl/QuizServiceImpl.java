package com.kiwit.backend.service.impl;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.QuizGroupAnswersDTO;
import com.kiwit.backend.dto.QuizGroupDTO;
import com.kiwit.backend.dto.QuizGroupWithQuizDTO;
import com.kiwit.backend.dto.QuizGroupWithSolvedDTO;
import com.kiwit.backend.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Override
    public List<QuizGroupDTO> getQuizGroup() {}
    @Override
    public List<QuizGroupWithQuizDTO> solveQuizGroup(Long groupId) {}
    @Override
    public QuizGroupWithSolvedDTO submitAnswers(Long quizId, QuizGroupAnswersDTO quizGroupAnswersDTO) {}
    @Override
    public QuizGroupWithSolvedDTO resubmitAnswers(Long quizId, QuizGroupAnswersDTO quizGroupAnswersDTO) {}
    @Override
    public QuizGroupWithSolvedDTO getQuizGroupLatestSolved(User authUser) {}
    @Override
    public List<QuizGroupWithSolvedDTO> getQuizKept(User authUser, Integer next, Integer limit) {}
    @Override
    public List<QuizGroupWithSolvedDTO> getQuizSolved(User authUser, Integer next, Integer limit) {}
}
