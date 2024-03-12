package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.LevelDAO;
import com.kiwit.backend.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LevelDAOImpl implements LevelDAO {

    private final LevelRepository levelRepository;

    @Autowired
    public LevelDAOImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }
}
