package com.kiwit.backend.repository;

import com.kiwit.backend.domain.TrophyAwarded;
import com.kiwit.backend.domain.compositeKey.TrophyAwardedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrophyAwardedRepository extends JpaRepository<TrophyAwarded, TrophyAwardedId> {
}
