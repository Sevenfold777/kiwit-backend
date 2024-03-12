package com.kiwit.backend.controller;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.QuizGroupDTO;
import com.kiwit.backend.dto.QuizGroupAnswersDTO;
import com.kiwit.backend.dto.QuizGroupWithQuizDTO;
import com.kiwit.backend.dto.QuizGroupWithSolvedDTO;
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
    public ResponseEntity<List<QuizGroupWithQuizDTO>>
    solveQuizGroup(@PathVariable Long groupId) {
        List<QuizGroupWithQuizDTO> resDto = quizService.solveQuizGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<QuizGroupWithSolvedDTO>
    submitAnswers(@PathVariable Long groupId,
                  @RequestBody QuizGroupAnswersDTO quizGroupAnswersDTO) {
        QuizGroupWithSolvedDTO resDto = quizService.submitAnswers(groupId, quizGroupAnswersDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PatchMapping("/group/{groupId}")
    public ResponseEntity<QuizGroupWithSolvedDTO>
    resubmitAnswers(@PathVariable Long groupId,
                    @RequestBody QuizGroupAnswersDTO quizGroupAnswersDTO) {
        QuizGroupWithSolvedDTO resDto = quizService.resubmitAnswers(groupId, quizGroupAnswersDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/group/latest-solved")
    public ResponseEntity<QuizGroupWithSolvedDTO>
    getQuizGroupLatestSolved(@AuthenticationPrincipal User authUser) {
        QuizGroupWithSolvedDTO resDto = quizService.getQuizGroupLatestSolved(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/kept")
    public ResponseEntity<List<QuizGroupWithSolvedDTO>>
    getQuizKept(@AuthenticationPrincipal User authUser,
                @RequestParam Integer next,
                @RequestParam Integer limit) {
        List<QuizGroupWithSolvedDTO> resDto = quizService.getQuizKept(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/solved")
    public ResponseEntity<List<QuizGroupWithSolvedDTO>>
    getQuizSolved(@AuthenticationPrincipal User authUser,
                  @RequestParam Integer next,
                  @RequestParam Integer limit) {
        List<QuizGroupWithSolvedDTO> resDto = quizService.getQuizSolved(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

}
