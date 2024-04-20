package com.kiwit.backend.service;

import com.kiwit.backend.domain.User;
import com.kiwit.backend.dto.*;

import java.util.List;

public interface ContentService {

    List<LevelDTO> getLevelList();
    List<ContentDTO> getLevelContent(Long levelId, Integer next, Integer limit);
    ContentWithPayloadDTO getContentPayload(Long contentId);
    ContentStudiedDTO studiedContent(User user, Long contentId);
    ContentStudiedDTO submitExercise(User user, Long contentId, ContentExerciseReqDTO contentExerciseReqDTO);
    List<CategoryDTO> getCategoryList();
    List<CategoryChapterWithContentDTO> getCategoryChapterWithContent(Long categoryId);
    ContentDTO getContentStudiedLatest(User user);
    List<ContentWithStudiedDTO> getContentKept(User user, Integer next, Integer limit);
    List<ContentWithStudiedDTO> getContentStudied(User user, Integer next, Integer limit);
    ContentStudiedDTO keepContent(User user, Long contentId);
}
