package com.kiwit.backend.controller;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/content")
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/level")
    public ResponseEntity<List<LevelDTO>>
    getLevelList() {
        List<LevelDTO> resDto = contentService.getLevelList();
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<ContentDTO>>
    getLevelContent(@PathVariable Long levelId) {
        List<ContentDTO> resDto = contentService.getLevelContent(levelId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentWithPayloadDTO>
    getContentPayload(@PathVariable Long contentId) {
        ContentWithPayloadDTO resDto = contentService.getContentPayload(contentId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PostMapping("/{contentId}")
    public ResponseEntity<ContentStudiedDTO>
    studyContent(@AuthenticationPrincipal User authUser,
                 @PathVariable Long contentId) {
        ContentStudiedDTO resDto = contentService.studyContent(authUser, contentId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PatchMapping("/{contentId}/exercise")
    public ResponseEntity<ContentStudiedDTO>
    submitExercise(@AuthenticationPrincipal User authUser,
                   @PathVariable Long contentId,
                   @RequestParam Boolean answer) {
        ContentStudiedDTO resDto = contentService.submitExercise(authUser, contentId, answer);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO>>
    getCategoryList() {
        List<CategoryDTO> resDto = contentService.getCategoryList();
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategoryChapterWithContentDTO>>
    getCategoryChapterWithContent(@PathVariable Long categoryId) {
        List<CategoryChapterWithContentDTO> resDto = contentService.getCategoryChapterWithContent(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/progress")
    public ResponseEntity<ContentDTO>
    getContentProgress(@AuthenticationPrincipal User authUser) {
        ContentDTO resDto = contentService.getContentProgress(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/kept")
    public ResponseEntity<List<ContentWithStudiedDTO>>
    getContentKept(@AuthenticationPrincipal User authUser,
                   @RequestParam Integer next,
                   @RequestParam Integer limit) {
        List<ContentWithStudiedDTO> resDto = contentService.getContentKept(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @GetMapping("/studied")
    public ResponseEntity<List<ContentWithStudiedDTO>>
    getContentStudied(@AuthenticationPrincipal User authUser,
                      @RequestParam Integer next,
                      @RequestParam Integer limit) {
        List<ContentWithStudiedDTO> resDto = contentService.getContentStudied(authUser, next, limit);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PatchMapping("/{contentId}/kept")
    public ResponseEntity<ContentStudiedDTO>
    keepContent(@AuthenticationPrincipal User authUser,
                @PathVariable Long contentId) {
        ContentStudiedDTO resDto = contentService.keepContent(authUser, contentId);
        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }


}
