package com.kiwit.backend.dao;

import com.kiwit.backend.domain.CategoryChapter;

import java.util.List;

public interface CategoryChapterDAO {

    List<CategoryChapter> selectCategoryChapterListWithContent(Long categoryId);
}
