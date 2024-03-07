package com.kiwit.backend.repository;

import com.kiwit.backend.domain.ContentStudied;
import com.kiwit.backend.domain.compositeKey.ContentStudiedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentStudiedRepository extends JpaRepository<ContentStudied, ContentStudiedId> {
}
