package com.market.bridge.repository;

import com.market.bridge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIdIsNull();
    List<Category> findByParentCategoryId(Long parentId);
}
