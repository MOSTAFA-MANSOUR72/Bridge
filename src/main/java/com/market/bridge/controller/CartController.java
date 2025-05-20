package com.market.bridge.controller;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.service.Cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
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


}
