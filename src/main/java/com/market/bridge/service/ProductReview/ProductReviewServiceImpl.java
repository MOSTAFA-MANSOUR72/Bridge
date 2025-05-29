package com.market.bridge.service.ProductReview;

import com.market.bridge.dto.ProductReview.ProductReviewAddRequest;
import com.market.bridge.dto.ProductReview.ProductReviewResponse;
import com.market.bridge.dto.ProductReview.ProductReviewUpdateRequest;
import com.market.bridge.entity.Product;
import com.market.bridge.entity.ProductReview;
import com.market.bridge.entity.users.Buyer;
import com.market.bridge.mapper.ProductReviewMapper;
import com.market.bridge.repository.BuyerRepo;
import com.market.bridge.repository.ProductRepo;
import com.market.bridge.repository.ProductReviewRepo;
import com.market.bridge.security.jwt.util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService{
    private final ProductReviewRepo productReviewRepo;
    private final ProductRepo productRepo;
    private final BuyerRepo buyerRepo;
    private final ProductReviewMapper productReviewMapper;

    @Override
    public ProductReviewResponse addProductReview(ProductReviewAddRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (productReviewRepo.existsByBuyerAndAndProduct(buyer, product)) {
            throw new IllegalArgumentException("You have already reviewed this product");
        }
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ProductReview productReview = ProductReview.builder()
                .review(request.getReview())
                .rating(request.getRating())
                .product(product)
                .buyer(buyer)
                .build();

        ProductReview review = productReviewRepo.save(productReview);
        return productReviewMapper.mapToEntity(review);
    }

    @Override
    public ProductReviewResponse updateProductReview(ProductReviewUpdateRequest request) {
        ProductReview productReview = productReviewRepo.findById(request.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("Product review not found"));

        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!productReview.getBuyer().getId().equals(util.userId)) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        productReview.setReview(request.getReview());
        productReview.setRating(request.getRating());

        return productReviewMapper.mapToEntity(productReviewRepo.save(productReview));
    }

    @Override
    public void deleteProductReview(Long id) {
        ProductReview productReview = productReviewRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product review not found"));

        if(!buyerRepo.existsById(util.userId)){
            throw new UsernameNotFoundException("User not found");
        }

        if (!productReview.getBuyer().getId().equals(util.userId)) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        productReviewRepo.delete(productReview);
    }

    @Override
    public Page<ProductReviewResponse> getProductReviewsByProductId(Long productId, Pageable pageable) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Page<ProductReviewResponse> productReviews = productReviewRepo.findByProduct(product, pageable).map(productReviewMapper::mapToEntity);
        return productReviews;
    }
}