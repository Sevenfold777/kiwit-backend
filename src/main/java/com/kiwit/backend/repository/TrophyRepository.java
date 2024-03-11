package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Trophy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrophyRepository extends JpaRepository<Trophy, Long> {
}
