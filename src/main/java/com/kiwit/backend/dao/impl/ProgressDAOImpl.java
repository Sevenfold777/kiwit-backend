package com.kiwit.backend.dao.impl;

import com.kiwit.backend.common.exception.CustomException;
import com.kiwit.backend.dao.ProgressDAO;
import com.kiwit.backend.domain.Progress;
import com.kiwit.backend.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgressDAOImpl implements ProgressDAO {

    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressDAOImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public Progress insertProgress(Progress progress) {
        try {
            return progressRepository.save(progress);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Progress selectProgressWithUser(Long userId) {
        Optional<Progress> progress = progressRepository.findProgressByUserId(userId);
        if (progress.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        return progress.get();
    }
}
