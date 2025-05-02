package com.market.bridge.repository;

import com.market.bridge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIdIsNull();
    List<Category> findByParentCategoryId(Long parentId);
}
