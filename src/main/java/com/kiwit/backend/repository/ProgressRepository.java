package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
