package com.kiwit.backend.dao;


import com.kiwit.backend.domain.Content;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentDAO {

    List<Content> selectContentListWithLevel(Long levelId);

    Content selectContentWithPayload(Long contentId);

    List<Content> selectContentListKept(Long userId, Integer next, Integer limit);

    List<Content> selectContentListStudied(Long userId, Integer next, Integer limit);

    Content selectContentWithProgress(Long userId);
}
