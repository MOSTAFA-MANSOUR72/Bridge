package com.market.bridge.mapper;

import com.market.bridge.dto.ProductReview.ProductReviewResponse;
import com.market.bridge.entity.ProductReview;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {
    public ProductReviewResponse mapToEntity(ProductReview productReview) {
        return ProductReviewResponse.builder()
                .id(productReview.getId())
                .productId(productReview.getProduct().getId())
                .review(productReview.getReview())
                .rating(productReview.getRating())
                .createdAt(productReview.getCreatedAt())
                .modifiedAt(productReview.getModifiedAt())
                .build();
    }
}
