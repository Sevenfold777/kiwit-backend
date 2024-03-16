package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.domain.Content;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.repository.ContentStudiedRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        try {
            return contentStudiedRepository.save(contentStudied);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @Override
    public ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer) {

        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));
        if (tgtStudy.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        tgtStudy.get().setMyAnswer(answer);

        return tgtStudy.get();
    }

    @Transactional
    @Override
    public ContentStudied keepContent(Long userId, Long contentId) {
        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));
        if (tgtStudy.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }

        ContentStudied study = tgtStudy.get();
        study.setKept(!tgtStudy.get().getKept());

        return study;
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
