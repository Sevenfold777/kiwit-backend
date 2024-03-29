package com.kiwit.backend.dao.impl;

import com.kiwit.backend.dao.CategoryDAO;
import com.kiwit.backend.domain.Category;
import com.kiwit.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDAOImpl implements CategoryDAO {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryDAOImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> selectCategoryList() {
        return this.categoryRepository.findAll();
    }
}
