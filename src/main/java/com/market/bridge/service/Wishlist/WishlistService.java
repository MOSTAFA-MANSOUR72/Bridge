package com.market.bridge.service.Wishlist;

import com.market.bridge.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistService {
    ProductResponse addToWishlist(Long productId);
    void removeFromWishlist(Long productId);
    void clearWishlist();

    Page<ProductResponse> getWishlist(Pageable pageable);
}
