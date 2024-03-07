package com.kiwit.backend.service.impl;

import com.kiwit.backend.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ContentServiceImpl implements ContentService {
    public void getContentPayload(Long contentId) {}
    public void getLevelList() {}
    public void getLevelChapterListWithContent(Long levelId) {}
}
