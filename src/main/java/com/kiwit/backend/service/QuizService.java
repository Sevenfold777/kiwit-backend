package com.kiwit.backend.service;

import org.springframework.web.bind.annotation.PathVariable;

public interface QuizService {

    void getQuiz(Long quizId);
    void getQuizList();
    void getQuizList(Long groupId);
}
