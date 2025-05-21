package com.market.bridge.mapper;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.dto.cartItem.CartItemUpdateRequest;
import com.market.bridge.entity.Product;
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
    public CartItem toCartItem(CartItemAddRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if (product.getQuantity()<request.getQuantity()) {
            throw new IllegalArgumentException("Not enough product in stock");
        }
        if (request.getQuantity()<=0 || request.getQuantity()<product.getMinOrder()) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        return CartItem.builder()
                .product(product)
                .quantity(request.getQuantity())
                .totalPrice(product.getPrice()*request.getQuantity())
                .build();
    }

    public CartItem toCartItem(CartItemUpdateRequest request, CartItem cartItem) {
        Product product = cartItem.getProduct();
        if (product.getQuantity()<request.getQuantity()) {
            throw new IllegalArgumentException("Not enough product in stock");
        }
        if (request.getQuantity()<=0 || request.getQuantity()<product.getMinOrder()) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItem.setTotalPrice(product.getPrice()*request.getQuantity());
        return cartItem;
    }
}
