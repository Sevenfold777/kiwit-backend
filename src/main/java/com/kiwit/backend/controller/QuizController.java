package com.kiwit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/quiz")
public class QuizController {

    @GetMapping("/group")
    public void getQuizGroup() {}

    @GetMapping("/group/{groupId}")
    public void getQuizGroup(@PathVariable Long groupId) {}

    @PostMapping("/group/{groupId}")
    public void submitAnswers(@PathVariable Long quizId) {}

    @PatchMapping("/group/{groupId}")
    public void resubmitAnswers(@PathVariable Long quizId) {}

    @GetMapping("/group/latest-solved")
    public void getQuizGroupLatestSolved() {}

    @GetMapping("/kept")
    public void getQuizKept() {}

    @GetMapping("/solved")
    public void getQuizSolved() {}

}
