package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ProgressDAO;
import com.kiwit.backend.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgressDAOImpl implements ProgressDAO {

    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressDAOImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }
}
