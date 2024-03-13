package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentDAO;
import com.kiwit.backend.domain.Content;
import com.kiwit.backend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ContentDAOImpl implements ContentDAO {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentDAOImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public List<Content> selectContentListWithLevel(Long levelId) {
        List<Content> contentList = contentRepository.findAllByLevelId(levelId);
        return contentList;
    }

    @Override
    public Content selectContentWithPayload(Long contentId)  {
        Optional<Content> content = contentRepository.findContentWithPayload(contentId);
        return content.get();
    }

    @Override
    public List<Content> selectContentListKept(Long userId, Integer next, Integer limit) {
        List<Content> contentList = contentRepository.findContentKept(userId);
        return contentList;
    }

    @Override
    public List<Content> selectContentListStudied(Long userId, Integer next, Integer limit) {
        List<Content> contentList = contentRepository.findContentStudied(userId);
        return contentList;
    }

    @Override
    public Content selectContentWithProgress(Long userId) {
        Content content = contentRepository.findByProgressUserId(userId);
        return  content;
    }
}
