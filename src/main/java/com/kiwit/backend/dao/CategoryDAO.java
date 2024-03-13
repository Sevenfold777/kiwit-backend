package com.kiwit.backend.dao;

import com.kiwit.backend.domain.Category;

import java.util.List;

public interface CategoryDAO {

    List<Category> selectCategoryList();
}
