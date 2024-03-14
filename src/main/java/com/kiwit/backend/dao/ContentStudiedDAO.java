package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Content;
import com.kiwit.backend.domain.ContentStudied;

import java.util.List;

public interface ContentStudiedDAO {

    ContentStudied insertContentStudied(ContentStudied contentStudied);

    ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer);

    ContentStudied keepContent(Long userId, Long contentId);

    List<ContentStudied> selectContentListKept(Long userId, Integer next, Integer limit);

    List<ContentStudied> selectContentListStudied(Long userId, Integer next, Integer limit);
}
