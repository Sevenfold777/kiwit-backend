package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ContentService {

    List<LevelDTO> getLevelList();
    List<ContentDTO> getLevelContent(Long levelId);
    ContentWithPayloadDTO getContentPayload(Long contentId);
    ContentWithStudiedDTO studyContent(User authUser, Long contentId);
    ContentWithStudiedDTO submitExercise(User authUser, Long contentId, Boolean answer);
    CategoryDTO getCategoryList();
    List<CategoryChapterWithContentDTO> getCategoryChapterWithContent(Long categoryId);
    ContentDTO getContentProgress(User authUser);
    List<ContentWithStudiedDTO> getContentKept(User authUser, Integer next, Integer limit);
    List<ContentWithStudiedDTO> getContentStudied(User authUser, Integer next, Integer limit);
}
