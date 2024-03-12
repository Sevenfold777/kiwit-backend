package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ContentPayloadDAO;
import com.kiwit.backend.repository.ContentPayloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentPayloadDAOImpl implements ContentPayloadDAO {

    private final ContentPayloadRepository contentPayloadRepository;

    @Autowired
    public ContentPayloadDAOImpl(ContentPayloadRepository contentPayloadRepository) {
        this.contentPayloadRepository = contentPayloadRepository;
    }
}
