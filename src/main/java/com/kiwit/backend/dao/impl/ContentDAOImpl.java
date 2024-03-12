package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentDAO;
import com.kiwit.backend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentDAOImpl implements ContentDAO {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentDAOImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }
}
