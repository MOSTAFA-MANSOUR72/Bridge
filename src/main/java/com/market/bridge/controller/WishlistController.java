package com.market.bridge.controller;

import com.market.bridge.service.Wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable long productId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishlistService.addToWishlist(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable long productId) {
        wishlistService.removeFromWishlist(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product removed from wishlist successfully.");
    }

    @DeleteMapping
    public ResponseEntity<?> clearWishlist() {
        wishlistService.clearWishlist();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Wishlist cleared successfully.");
    }

    @GetMapping
    public ResponseEntity<?> getWishlist(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(wishlistService.getWishlist(Pageable.ofSize(pageSize).withPage(pageNumber)));
    }
}
