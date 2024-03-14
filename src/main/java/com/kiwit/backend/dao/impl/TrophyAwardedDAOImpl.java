package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrophyAwardedDAOImpl implements TrophyAwardedDAO {
    private final TrophyAwardedRepository trophyAwardedRepository;

    @Autowired
    public TrophyAwardedDAOImpl(TrophyAwardedRepository trophyAwardedRepository) {
        this.trophyAwardedRepository = trophyAwardedRepository;
    }

    @Override
    public List<TrophyAwarded> selectMyTrophyAwarded(Long userId) {
        List<TrophyAwarded> trophyAwardedList = trophyAwardedRepository.findTrophyAwarded(userId);
        return trophyAwardedList;
    }

    @Override
    public TrophyAwarded selectTrophyAwardedLatest(Long userId) {
        Optional<TrophyAwarded> trophyAwarded = trophyAwardedRepository.findTrophyAwardedLatest(userId);
        return trophyAwarded.get();
    }
}
