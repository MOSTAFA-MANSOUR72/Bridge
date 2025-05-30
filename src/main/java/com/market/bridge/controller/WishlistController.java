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
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(wishlistService.addToWishlist(productId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable long productId) {
        try {
            wishlistService.removeFromWishlist(productId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Product removed from wishlist successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearWishlist() {
        try {
            wishlistService.clearWishlist();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Wishlist cleared successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getWishlist(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(wishlistService.getWishlist(Pageable.ofSize(pageSize).withPage(pageNumber)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
