package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.CategoryChapterDAO;
import com.kiwit.backend.domain.CategoryChapter;
import com.kiwit.backend.repository.CategoryChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryChapterDAOImpl implements CategoryChapterDAO {

    private final CategoryChapterRepository categoryChapterRepository;

    @Autowired
    public CategoryChapterDAOImpl(CategoryChapterRepository categoryChapterRepository) {
        this.categoryChapterRepository = categoryChapterRepository;
    }

    @Override
    public List<CategoryChapter> selectCategoryChapterListWithContent(Long categoryId) {
        return this.categoryChapterRepository.findByCategoryId(categoryId);
    }
}
