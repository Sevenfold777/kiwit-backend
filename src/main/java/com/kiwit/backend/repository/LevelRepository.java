package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Long> {
}
