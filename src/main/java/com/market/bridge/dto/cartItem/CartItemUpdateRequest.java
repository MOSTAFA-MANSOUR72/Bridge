package com.market.bridge.dto.cartItem;

import lombok.Data;

@Data
public class CartItemUpdateRequest {
    private Long cartItemId;
    private Long quantity;
}
