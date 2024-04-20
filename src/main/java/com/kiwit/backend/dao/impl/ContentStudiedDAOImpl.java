package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.repository.ContentStudiedRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ContentStudied saveContentStudied(ContentStudied contentStudied) {
        return contentStudiedRepository.save(contentStudied);
    }

    @Transactional
    @Override
    public ContentStudied updateExerciseAnswer(Long userId, Long contentId, Boolean answer) {
        ContentStudied contentStudied = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId))
                .orElseThrow(() -> new DataAccessException("Cannot find ContentStudied with userId and contentId.") {});
        contentStudied.setMyAnswer(answer);

        return contentStudied;
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
        Pageable pageable = PageRequest.of(next, limit);
        return contentStudiedRepository.findContentKept(userId, pageable);
    }

    @Override
    public List<ContentStudied> selectContentListStudied(Long userId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return contentStudiedRepository.findContentStudied(userId, pageable);
    }
}
