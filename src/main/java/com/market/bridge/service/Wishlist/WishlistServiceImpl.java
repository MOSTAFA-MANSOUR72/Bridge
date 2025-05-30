package com.market.bridge.service.Wishlist;

import com.market.bridge.dto.product.ProductResponse;
import com.market.bridge.entity.Product;
import com.market.bridge.entity.users.Buyer;
import com.market.bridge.mapper.ProductMapper;
import com.market.bridge.repository.BuyerRepo;
import com.market.bridge.repository.ProductRepo;
import com.market.bridge.security.jwt.util;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
    private final BuyerRepo buyerRepo;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    @Override
    public ProductResponse addToWishlist(Long productId) {
        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("Buyer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        buyer.addProductToWishlist(product);
        buyerRepo.save(buyer);

        return productMapper.toProductResponse(product);
    }

    @Override
    public void removeFromWishlist(Long productId) {
        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("Buyer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        buyer.getWishlist().remove(product);
        buyerRepo.save(buyer);
    }

    @Override
    public void clearWishlist() {
        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("Buyer not found"));

        buyer.getWishlist().clear();
        buyerRepo.save(buyer);
    }

    @Override
    public Page<ProductResponse> getWishlist(Pageable pageable) {
        Buyer buyer = buyerRepo.findById(util.userId)
                .orElseThrow(() -> new UsernameNotFoundException("Buyer not found"));

        Page<Product> wishlistProducts = productRepo.findBuyerWishlistProducts(util.userId, pageable);

        return wishlistProducts.map(productMapper::toProductResponse);
    }
}
