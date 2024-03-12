package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.TrophyDAO;
import com.kiwit.backend.repository.TrophyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrophyDAOImpl implements TrophyDAO {
    private final TrophyRepository trophyRepository;

    @Autowired
    public TrophyDAOImpl(TrophyRepository trophyRepository) {
        this.trophyRepository = trophyRepository;
    }
}
