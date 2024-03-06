package com.kiwit.backend.repository;

import com.kiwit.backend.domain.ContentPayload;
import com.kiwit.backend.domain.ContentPayloadId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ContentPayloadRepository extends JpaRepository<ContentPayload, ContentPayloadId> {
}
