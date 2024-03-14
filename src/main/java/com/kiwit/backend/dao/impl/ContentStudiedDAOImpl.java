package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.domain.Content;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.repository.ContentStudiedRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ContentStudiedDAOImpl implements ContentStudiedDAO {

    private final ContentStudiedRepository contentStudiedRepository;

    @Autowired
    public ContentStudiedDAOImpl(ContentStudiedRepository contentStudiedRepository) {
        this.contentStudiedRepository = contentStudiedRepository;
    }

    @Override
    public ContentStudied insertContentStudied(ContentStudied contentStudied) {

        ContentStudied savedStudy = contentStudiedRepository.save(contentStudied);
        return savedStudy;
    }

    @Transactional
    @Override
    public ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer) {

        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));
        // exception handling needed
        tgtStudy.get().setMyAnswer(answer);

        return tgtStudy.get();
    }

    @Transactional
    @Override
    public ContentStudied keepContent(Long userId, Long contentId) {
        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));

        ContentStudied study = tgtStudy.get();
        study.setKept(!tgtStudy.get().getKept());

        return study;
    }

    @Override
    public List<ContentStudied> selectContentListKept(Long userId, Integer next, Integer limit) {
        List<ContentStudied> contentStudiedList = contentStudiedRepository.findContentKept(userId);
        return contentStudiedList;
    }

    @Override
    public List<ContentStudied> selectContentListStudied(Long userId, Integer next, Integer limit) {
        List<ContentStudied> contentStudiedList = contentStudiedRepository.findContentStudied(userId);
        return contentStudiedList;
    }
}
