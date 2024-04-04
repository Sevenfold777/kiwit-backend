package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.TrophyAwardedDAO;
import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.repository.TrophyAwardedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public List<TrophyAwarded> selectMyTrophyAwarded(Long userId, Integer next, Integer limit) {
        Pageable pageable = PageRequest.of(next, limit);
        return trophyAwardedRepository.findTrophyAwarded(userId, pageable);
    }

    @Override
    public TrophyAwarded selectTrophyAwardedLatest(Long userId) {
        return trophyAwardedRepository.findTrophyAwardedLatest(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NO_CONTENT));
    }
}
