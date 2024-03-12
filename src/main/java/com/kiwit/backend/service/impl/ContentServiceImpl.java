package com.kiwit.backend.service.impl;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Override
    public List<LevelDTO> getLevelList() {

    }

    @Override
    public List<ContentDTO> getLevelContent(Long levelId) {

    }
    @Override
    public ContentWithPayloadDTO getContentPayload(Long contentId) {

    }
    @Override
    public ContentWithStudiedDTO studyContent(User authUser, Long contentId) {

    }
    @Override
    public ContentWithStudiedDTO submitExercise(User authUser, Long contentId, Boolean answer) {

    }
    @Override
    public CategoryDTO getCategoryList() {

    }
    @Override
    public List<CategoryChapterWithContentDTO> getCategoryChapterWithContent(Long categoryId) {

    }
    @Override
    public ContentDTO getContentProgress(User authUser) {

    }
    @Override
    public List<ContentWithStudiedDTO> getContentKept(User authUser, Integer next, Integer limit) {

    }
    @Override
    public List<ContentWithStudiedDTO> getContentStudied(User authUser, Integer next, Integer limit) {

    }
}