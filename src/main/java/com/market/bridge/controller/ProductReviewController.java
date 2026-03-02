package com.market.bridge.controller;

import com.market.bridge.dto.ProductReview.ProductReviewAddRequest;
import com.market.bridge.dto.ProductReview.ProductReviewUpdateRequest;
import com.market.bridge.service.ProductReview.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-review")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    @PostMapping
    public ResponseEntity<?> addProductReview(@RequestBody ProductReviewAddRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productReviewService.addProductReview(request));
    }

    @PutMapping
    public ResponseEntity<?> updateProductReview(@RequestBody ProductReviewUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productReviewService.updateProductReview(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductReview(@PathVariable Long id) {
        productReviewService.deleteProductReview(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product review deleted successfully.");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductReviews(@PathVariable Long productId,
                                               @RequestParam(defaultValue = "0") int pageNumber,
                                               @RequestParam(defaultValue = "50") int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productReviewService.getProductReviewsByProductId(productId, pageable));
    }
}
