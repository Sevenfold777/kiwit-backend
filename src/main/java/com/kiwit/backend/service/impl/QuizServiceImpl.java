package com.kiwit.backend.service.impl;

import com.kiwit.backend.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class QuizServiceImpl implements QuizService {

    @Override
    public void getQuizGroup() {}
    @Override
    public void getQuizGroup(Long groupId) {}
    @Override
    public void submitAnswers(Long quizId) {}
    @Override
    public void resubmitAnswers(Long quizId) {}
    @Override
    public void getQuizGroupLatestSolved() {}
    @Override
    public void getQuizKept() {}
    @Override
    public void getQuizSolved() {}
}
