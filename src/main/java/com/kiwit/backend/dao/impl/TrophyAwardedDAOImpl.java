package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrophyAwardedDAOImpl implements TrophyAwardedDAO {
    private final TrophyAwardedRepository trophyAwardedRepository;

    @Autowired
    public TrophyAwardedDAOImpl(TrophyAwardedRepository trophyAwardedRepository) {
        this.trophyAwardedRepository = trophyAwardedRepository;
    }

    @Override
    public List<TrophyAwarded> selectMyTrophyAwarded(Long userId) {
        return null;
    }

    @Override
    public TrophyAwarded selectTrophyAwardedLatest(Long userId) {
        return null;
    }
}
