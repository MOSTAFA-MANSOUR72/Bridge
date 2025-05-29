package com.market.bridge.dto.ProductReview;

import lombok.Data;

@Data
public class ProductReviewUpdateRequest {
    private Long reviewId;
    private String review;
    private float rating;
}
