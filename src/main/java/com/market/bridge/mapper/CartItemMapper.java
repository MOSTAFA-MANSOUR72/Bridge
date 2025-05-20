package com.market.bridge.mapper;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.entity.cart.Cart;
import com.market.bridge.entity.cart.CartItem;
import com.market.bridge.repository.ProductRepo;
import com.market.bridge.service.Product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemMapper {
    private final ProductRepo productRepo;
    public CartItem toCartItem(CartItemAddRequest request){
        return CartItem.builder()
                .product(productRepo.findById(request.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found")))
                .quantity(request.getQuantity())
                .totalPrice(request.getItemPrice()*request.getQuantity())
                .build();
    }
}
