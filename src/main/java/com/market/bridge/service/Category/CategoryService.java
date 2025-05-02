package com.market.bridge.service.Category;

import com.market.bridge.dto.category.CategoryAddRequest;
import com.market.bridge.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getParentCategories();
    List<CategoryDTO> getChildCategories(Long parentId);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryAddRequest category);
    CategoryDTO updateCategory(CategoryDTO category);
    String deleteCategory(Long id);
}
