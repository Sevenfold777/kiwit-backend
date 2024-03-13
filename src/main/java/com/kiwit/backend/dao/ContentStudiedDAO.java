package com.kiwit.backend.dao;

import com.kiwit.backend.domain.ContentStudied;

public interface ContentStudiedDAO {

    ContentStudied insertContentStudied(ContentStudied contentStudied);

    ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer);

    ContentStudied keepContent(Long userId, Long contentId);
}
