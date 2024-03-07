package com.kiwit.backend.repository;

import com.kiwit.backend.domain.ContentPayload;
import com.kiwit.backend.domain.compositeKey.ContentPayloadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentPayloadRepository extends JpaRepository<ContentPayload, ContentPayloadId> {
}
