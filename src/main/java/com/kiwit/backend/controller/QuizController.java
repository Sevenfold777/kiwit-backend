package com.kiwit.backend.controller;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/group")
    public ResponseEntity<List<QuizGroupDTO>>
    getQuizGroup(@RequestParam Integer next,
                 @RequestParam Integer limit,
                 @RequestParam String tag) {
        List<QuizGroupDTO> resDto = quizService.getQuizGroup();
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<QuizGroupWithQuizDTO>
    solveQuizGroup(@PathVariable Long groupId) {
        QuizGroupWithQuizDTO resDto = quizService.solveQuizGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<QuizGroupSolvedDTO>
    submitAnswers(@AuthenticationPrincipal User authUser,
                  @PathVariable Long groupId,
                  @RequestBody QuizAnswerListDTO quizAnswerListDTO) {
        QuizGroupSolvedDTO resDto = quizService.submitAnswers(authUser.getId(), groupId, quizAnswerListDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PatchMapping("/group/{groupId}")
    public ResponseEntity<QuizGroupSolvedDTO>
    resubmitAnswers(@AuthenticationPrincipal User authUser,
                    @PathVariable Long groupId,
                    @RequestBody QuizAnswerListDTO quizAnswerListDTO) {
        QuizGroupSolvedDTO resDto = quizService.resubmitAnswers(authUser.getId(), groupId, quizAnswerListDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/group/latest-solved")
    public ResponseEntity<QuizGroupWithSolvedDTO>
    getQuizGroupLatestSolved(@AuthenticationPrincipal User authUser) {
        QuizGroupWithSolvedDTO resDto = quizService.getQuizGroupLatestSolved(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/kept")
    public ResponseEntity<List<QuizWithSolvedDTO>>
    getQuizKept(@AuthenticationPrincipal User authUser,
                @RequestParam Integer next,
                @RequestParam Integer limit) {
        List<QuizWithSolvedDTO> resDto = quizService.getQuizKept(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/solved")
    public ResponseEntity<List<QuizWithSolvedDTO>>
    getQuizSolved(@AuthenticationPrincipal User authUser,
                  @RequestParam Integer next,
                  @RequestParam Integer limit) {
        List<QuizWithSolvedDTO> resDto = quizService.getQuizSolved(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PatchMapping("/{quizId}/kept")
    public ResponseEntity<QuizSolvedDTO>
    keepContent(@AuthenticationPrincipal User authUser,
                @PathVariable Long quizId) {
        QuizSolvedDTO resDto = quizService.keepQuiz(authUser, quizId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

}
