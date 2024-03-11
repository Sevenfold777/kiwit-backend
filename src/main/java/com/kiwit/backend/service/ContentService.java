package com.kiwit.backend.service;

import org.springframework.web.bind.annotation.PathVariable;

public interface ContentService {

    void getLevelList();
    void getLevelChapterListWithContent(Long levelId);
    void getContentPayload(Long contentId);
    void studyContent(Long contentId);
    void submitExercise(Long contentId);
    void getCategoryList();
    void getCategoryChapterWithContent();
    void getContentProgress();
    void getContentKept();
    void getContentStudied();
}
