package com.market.bridge.service.ProductReview;

import com.market.bridge.dto.ProductReview.ProductReviewAddRequest;
import com.market.bridge.dto.ProductReview.ProductReviewResponse;
import com.market.bridge.dto.ProductReview.ProductReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductReviewService {
    ProductReviewResponse addProductReview(ProductReviewAddRequest request);
    ProductReviewResponse updateProductReview(ProductReviewUpdateRequest request);
    void deleteProductReview(Long id);
    Page<ProductReviewResponse> getProductReviewsByProductId(Long productId, Pageable pageable);
}
