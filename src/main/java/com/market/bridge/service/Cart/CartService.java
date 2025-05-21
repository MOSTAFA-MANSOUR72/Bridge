package com.market.bridge.service.Cart;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.dto.cartItem.CartItemUpdateRequest;
import com.market.bridge.entity.cart.Cart;
import com.market.bridge.entity.cart.CartItem;

import java.util.List;

public interface CartService {
     CartItem addToCart(CartItemAddRequest request);

     List<CartItem> getAllCartItems();
     CartItem updateCartItem(CartItemUpdateRequest request);
     void deleteCartItem(Long cartItemId);
     Cart getOrCreateCart();
     Cart createNewCart();
}
