package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrophyAwardedDAOImpl implements TrophyAwardedDAO {
    private final TrophyAwardedRepository trophyAwardedRepository;

    @Autowired
    public TrophyAwardedDAOImpl(TrophyAwardedRepository trophyAwardedRepository) {
        this.trophyAwardedRepository = trophyAwardedRepository;
    }
}
