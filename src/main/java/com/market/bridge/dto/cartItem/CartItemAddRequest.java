package com.market.bridge.dto.cartItem;
import lombok.Data;

@Data
public class CartItemAddRequest {
    private Long productId;
    private Long quantity;
}
