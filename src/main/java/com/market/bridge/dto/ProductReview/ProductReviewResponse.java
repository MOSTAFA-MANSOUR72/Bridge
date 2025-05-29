package com.market.bridge.dto.ProductReview;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ProductReviewResponse {
    private Long id;
    private Long productId;
    private String review;
    private float rating;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}
