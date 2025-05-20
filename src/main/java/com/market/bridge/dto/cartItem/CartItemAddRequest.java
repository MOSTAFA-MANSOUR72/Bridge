package com.market.bridge.dto.cartItem;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CartItemAddRequest {
    private Long productId;
    private Long quantity;
    private Long itemPrice;
}
