package com.kiwit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/content")
public class ContentController {

    @Operation(summary = "Content with Payload", description = "content with joined payload")
    @GetMapping("/{contentId}")
    public void getContentPayload(@PathVariable Long contentId) {}

    @Operation(summary = "Level list", description = "level list")
    @GetMapping("/level")
    public void getLevelList() {}

    @Operation(summary = "Level Chapter list with Content",
            description = "level Chapter list joined with Content (without payload)")
    @GetMapping("/level/{levelId}")
    public void getLevelChapterListWithContent(@PathVariable Long levelId) {}


}
