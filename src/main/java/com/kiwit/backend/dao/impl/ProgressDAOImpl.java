package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.ProgressDAO;
import com.kiwit.backend.domain.Progress;
import com.kiwit.backend.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;


@Component
public class ProgressDAOImpl implements ProgressDAO {

    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressDAOImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public Progress insertProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    @Override
    public Progress selectProgressWithUser(Long userId) {
        return progressRepository.findProgressByUserId(userId)
                .orElseThrow(() -> new DataAccessException("Cannot find Progress with userId.") {});
    }
}
