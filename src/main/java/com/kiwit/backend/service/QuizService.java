package com.kiwit.backend.service;

import org.springframework.web.bind.annotation.PathVariable;

public interface QuizService {

    void getQuizGroup();
    void getQuizGroup(Long groupId);
    void submitAnswers(Long quizId);
    void resubmitAnswers(Long quizId);
    void getQuizGroupLatestSolved();
    void getQuizKept();
    void getQuizSolved();
}
