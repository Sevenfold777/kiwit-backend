package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;

import java.util.List;

public interface ContentService {

    List<LevelDTO> getLevelList();
    List<ContentDTO> getLevelContent(Long levelId);
    ContentWithPayloadDTO getContentPayload(Long contentId);
    ContentStudiedDTO studyContent(User authUser, Long contentId);
    ContentStudiedDTO submitExercise(User authUser, Long contentId, Boolean answer);
    List<CategoryDTO> getCategoryList();
    List<CategoryChapterWithContentDTO> getCategoryChapterWithContent(Long categoryId);
    ContentDTO getContentProgress(User authUser);
    List<ContentWithStudiedDTO> getContentKept(User authUser, Integer next, Integer limit);
    List<ContentWithStudiedDTO> getContentStudied(User authUser, Integer next, Integer limit);
    ContentStudiedDTO keepContent(User authUser, Long contentId);
}
