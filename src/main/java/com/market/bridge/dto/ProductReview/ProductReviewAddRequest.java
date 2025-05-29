package com.market.bridge.dto.ProductReview;

import lombok.Data;

@Data
public class ProductReviewAddRequest {
    private Long productId;
    private String review;
    private float rating;
}
