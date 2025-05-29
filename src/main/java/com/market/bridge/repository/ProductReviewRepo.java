package com.market.bridge.repository;

import com.market.bridge.entity.Product;
import com.market.bridge.entity.ProductReview;
import com.market.bridge.entity.users.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepo extends JpaRepository<ProductReview, Long> {
    Boolean existsByBuyerAndAndProduct(Buyer buyer, Product product);
    Page<ProductReview> findByProduct(Product product, Pageable pageable);
}
