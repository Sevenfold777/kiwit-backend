package com.kiwit.backend.repository;

import com.kiwit.backend.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
