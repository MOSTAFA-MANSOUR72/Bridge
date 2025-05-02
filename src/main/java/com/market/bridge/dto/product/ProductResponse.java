package com.market.bridge.dto.product;

import com.market.bridge.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long minOrder;
    private Long quantity;
    private Integer rating;
    private List<String> images;
    private List<Long> categoriesIdes;
    private String brandName;
    private Long sellerId;
}
