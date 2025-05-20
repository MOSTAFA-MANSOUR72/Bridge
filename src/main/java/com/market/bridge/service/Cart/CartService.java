package com.market.bridge.service.Cart;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.entity.cart.Cart;
import com.market.bridge.entity.cart.CartItem;

import java.util.List;

public interface CartService {
     CartItem addToCart(CartItemAddRequest request);

     List<CartItem> getAllCartItems();
     Cart getOrCreateCart();
     Cart createNewCart();
}
