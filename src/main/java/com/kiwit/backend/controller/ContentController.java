package com.kiwit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/content")
public class ContentController {

    @GetMapping("/level")
    public void getLevelList() {}

    @GetMapping("/level/{levelId}")
    public void getLevelChapterListWithContent(@PathVariable Long levelId) {}

    @GetMapping("/{contentId}")
    public void getContentPayload(@PathVariable Long contentId) {}

    @PostMapping("/{contentId}")
    public void studyContent(@PathVariable Long contentId) {}

    @PatchMapping("/{contentId}/exercise")
    public void submitExercise(@PathVariable Long contentId) {}

    @GetMapping("/category")
    public void getCategoryList() {}

    @GetMapping("/category/{categoryId}")
    public void getCategoryChapterWithContent() {}

    @GetMapping("/progress")
    public void getContentProgress() {}

    @GetMapping("/kept")
    public void getContentKept() {}

    @GetMapping("/studied")
    public void getContentStudied() {}


}
