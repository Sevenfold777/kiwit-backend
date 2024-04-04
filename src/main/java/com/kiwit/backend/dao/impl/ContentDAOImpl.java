package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentDAO;
import com.kiwit.backend.domain.Content;
import com.kiwit.backend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentDAOImpl implements ContentDAO {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentDAOImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public List<Content> selectContentListWithLevel(Long levelId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return contentRepository.findAllByLevelId(levelId, pageable);
    }

    @Override
    public Content selectContentWithPayload(Long contentId)  {
        return contentRepository.findContentWithPayload(contentId)
                .orElseThrow(() -> new DataAccessException("Cannot find Content with contentId.") {});
    }

    @Override
    public Content selectStudiedLatest(Long userId) {
        // TODO?: separate cause of empty
        // 1) bad request, 2) all studied
        return contentRepository.findStudiedLatest(userId)
                .orElseThrow(() -> new DataAccessException("Cannot find ContentStudied with userId and contentId.") {});
    }
}
