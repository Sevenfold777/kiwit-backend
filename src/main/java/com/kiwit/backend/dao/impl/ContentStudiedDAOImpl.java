package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.domain.Content;
import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import com.kiwit.backend.repository.ContentStudiedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    @Override
    public ContentStudied updateContentStudied(Long userId, Long contentId, Boolean answer) {

        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));


        ContentStudied updatedStudy;
        if (tgtStudy.isPresent()) {
            ContentStudied study = tgtStudy.get();

            study.setMyAnswer(answer);

            updatedStudy = contentStudiedRepository.save(study);
        } else {
            return  null;
//            throw new Exception();
        }

        return updatedStudy;
    }

    @Override
    public ContentStudied keepContent(Long userId, Long contentId) {
        Optional<ContentStudied> tgtStudy = contentStudiedRepository.findById(new ContentStudiedId(userId, contentId));


        ContentStudied updatedStudy;
        if (tgtStudy.isPresent()) {
            ContentStudied study = tgtStudy.get();

            study.setKept(!tgtStudy.get().getKept());

            updatedStudy = contentStudiedRepository.save(study);
        } else {
            return  null;
//            throw new Exception();
        }

        return updatedStudy;
    }
}
