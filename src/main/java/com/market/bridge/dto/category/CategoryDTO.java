package com.market.bridge.dto.category;

import com.market.bridge.entity.Category;
import lombok.Data;

// Used in Category response and Category update request
@Data
public class CategoryDTO {
    private Long id;
    private Long parentCategoryId;
    private String name;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.parentCategoryId = category.getParentCategoryId();
        this.name = category.getName();
    }
}
