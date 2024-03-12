package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.CategoryChapterDAO;
import com.kiwit.backend.repository.CategoryChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryChapterDAOImpl implements CategoryChapterDAO {

    private final CategoryChapterRepository categoryChapterRepository;

    @Autowired
    public CategoryChapterDAOImpl(CategoryChapterRepository categoryChapterRepository) {
        this.categoryChapterRepository = categoryChapterRepository;
    }
}
