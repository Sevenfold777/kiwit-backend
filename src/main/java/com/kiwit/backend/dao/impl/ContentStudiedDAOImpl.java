package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.repository.ContentStudiedRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentStudiedDAOImpl implements ContentStudiedDAO {

    private final ContentStudiedRepository contentStudiedRepository;

    @Autowired
    public ContentStudiedDAOImpl(ContentStudiedRepository contentStudiedRepository) {
        this.contentStudiedRepository = contentStudiedRepository;
    }

    @Override
    public ContentStudied insertContentStudied(ContentStudied contentStudied) {
        return contentStudiedRepository.save(contentStudied);
    }

    @Transactional
    @Override
    public ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer) {
        return contentStudiedRepository.findById(new ContentStudiedId(userId, contentId))
                .orElseThrow(() -> new DataAccessException("Cannot find ContentStudied with userId and contentId.") {});
    }

    @Transactional
    @Override
    public ContentStudied keepContent(Long userId, Long contentId) {
        ContentStudied contentStudied = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId))
                .orElseThrow(() -> new DataAccessException("Cannot find ContentStudied with userId and contentId.") {});;

        contentStudied.setKept(!contentStudied.getKept());

        return contentStudied;
    }

    @Override
    public List<ContentStudied> selectContentListKept(Long userId, Integer next, Integer limit) {
        return contentStudiedRepository.findContentKept(userId);
    }

    @Override
    public List<ContentStudied> selectContentListStudied(Long userId, Integer next, Integer limit) {
        return contentStudiedRepository.findContentStudied(userId);
    }
}
