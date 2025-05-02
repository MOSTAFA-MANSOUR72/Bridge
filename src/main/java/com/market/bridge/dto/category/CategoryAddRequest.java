package com.market.bridge.dto.category;

import lombok.Data;

@Data
public class CategoryAddRequest {
    private Long parentCategoryId;
    private String name;
}
