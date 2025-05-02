package com.market.bridge.service.Category;

import com.market.bridge.dto.category.CategoryAddRequest;
import com.market.bridge.dto.category.CategoryDTO;
import com.market.bridge.entity.Category;
import com.market.bridge.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    @Override
    public List<CategoryDTO> getParentCategories() {
        return categoryRepo.findByParentCategoryIdIsNull().stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getChildCategories(Long parentId) {
        return categoryRepo.findByParentCategoryId(parentId).stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .map(CategoryDTO::new)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public CategoryDTO createCategory(CategoryAddRequest category) {
        Category newCategory = Category.builder()
                .name(category.getName())
                .parentCategoryId(category.getParentCategoryId())
                .build();
        categoryRepo.save(newCategory);
        return new CategoryDTO(newCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category) {
        Category existingCategory = categoryRepo.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingCategory.setParentCategoryId(category.getParentCategoryId());
        existingCategory.setName(category.getName());
        categoryRepo.save(existingCategory);
        return category;
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepo.delete(category);
        return "Category deleted successfully";
    }
}
