package com.market.bridge.controller;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.dto.cartItem.CartItemUpdateRequest;
import com.market.bridge.service.Cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BUYER')")
public class CartController {
    private final CartService cartService;
    @GetMapping
    public ResponseEntity<?> getAllCartItems(){
        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(
                        cartService.getAllCartItems()
                );
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartItemAddRequest request) {
        return ResponseEntity.ok(
                cartService.addToCart(request)
        );
    }

    @PutMapping
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemUpdateRequest request) {
        return ResponseEntity.ok(
                cartService.updateCartItem(request)
        );
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok("Cart item deleted successfully");
    }


}
