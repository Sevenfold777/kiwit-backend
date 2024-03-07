package com.kiwit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/quiz")
public class QuizController {

    @Operation(summary = "Quiz with choices", description = "quiz with joined choices")
    @GetMapping("/{quizId}")
    public void getQuiz(@PathVariable Long quizId) {}

    @Operation(summary = "Quiz Group List", description = "group list")
    @GetMapping("/group")
    public void getQuizList() {}

    @Operation(summary = "Quiz List in a Group", description = "quiz list in ")
    @GetMapping("/group/{groupId}")
    public void getQuizList(@PathVariable Long groupId) {}


}
