package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentStudiedDAO;
import com.kiwit.backend.repository.ContentStudiedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentStudiedDAOImpl implements ContentStudiedDAO {

    private final ContentStudiedRepository contentStudiedRepository;

    @Autowired
    public ContentStudiedDAOImpl(ContentStudiedRepository contentStudiedRepository) {
        this.contentStudiedRepository = contentStudiedRepository;
    }
}
