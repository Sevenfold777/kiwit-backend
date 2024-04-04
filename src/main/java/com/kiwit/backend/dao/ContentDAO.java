package com.kiwit.backend.dao;


import com.kiwit.backend.domain.Content;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentDAO {

    List<Content> selectContentListWithLevel(Long levelId, Integer next, Integer limit);

    Content selectContentWithPayload(Long contentId);

    Content selectStudiedLatest(Long userId);
}
