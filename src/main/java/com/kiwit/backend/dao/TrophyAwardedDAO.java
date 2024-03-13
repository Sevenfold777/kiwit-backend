package com.kiwit.backend.dao;

import com.kiwit.backend.domain.TrophyAwarded;

import java.util.List;

public interface TrophyAwardedDAO {

    List<TrophyAwarded> selectMyTrophyAwarded(Long userId);

    TrophyAwarded selectTrophyAwardedLatest(Long userId);
}
