package com.kiwit.backend.service;

import org.springframework.web.bind.annotation.PathVariable;

public interface ContentService {

    void getContentPayload(Long contentId);
    void getLevelList();
    void getLevelChapterListWithContent(Long levelId);
}
