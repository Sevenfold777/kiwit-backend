package com.kiwit.backend.dao;


import com.kiwit.backend.domain.Content;

import java.util.List;

public interface ContentDAO {

    List<Content> selectContentListWithLevel(Long levelId, Integer next, Integer limit);

    Content selectContent(Long contentId);

    Content selectStudiedLatest(Long userId);

    Content selectContentWithStudied(Long contentId, Long userId);

    Content getContentProxy(Long contentId);
}
