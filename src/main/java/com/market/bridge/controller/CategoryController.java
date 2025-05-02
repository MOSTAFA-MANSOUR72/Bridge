package com.market.bridge.controller;

import com.market.bridge.dto.category.CategoryAddRequest;
import com.market.bridge.dto.category.CategoryDTO;
import com.market.bridge.service.Category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getPathCategories() {
        return ResponseEntity.ok(categoryService.getParentCategories());
    }

    @GetMapping("/child/{parentId}")
    public ResponseEntity<?> getChildCategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getChildCategories(parentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryAddRequest category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping()
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO category) {
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
