package com.market.bridge.service.Cart;

import com.market.bridge.dto.cartItem.CartItemAddRequest;
import com.market.bridge.dto.cartItem.CartItemUpdateRequest;
import com.market.bridge.entity.Product;
import com.market.bridge.entity.cart.Cart;
import com.market.bridge.entity.cart.CartItem;
import com.market.bridge.mapper.CartItemMapper;
import com.market.bridge.repository.BuyerRepo;
import com.market.bridge.repository.CartItemRepo;
import com.market.bridge.repository.CartRepo;
import com.market.bridge.repository.ProductRepo;
import com.market.bridge.security.jwt.util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepo cartRepo;
    private final BuyerRepo buyerRepo;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;


    @Override
    public CartItem addToCart(CartItemAddRequest request) {
        Cart cart = getOrCreateCart();
        CartItem cartItem = cartItemMapper.toCartItem(request);
        cartItem.setCart(cart);
        cart.addCartItem(cartItem);

        cartItemRepo.save(cartItem);
        cartRepo.save(cart);
        return cartItem;
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return getOrCreateCart().getCartItems();
    }

    @Override
    public CartItem updateCartItem(CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepo.findById(request.getCartItemId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cartItemMapper.toCartItem(request, cartItem);

        return cartItemRepo.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());

        cartItemRepo.delete(cartItem);
        productRepo.save(product);
    }

    @Override
    public Cart getOrCreateCart() {
        Optional<Cart> cart = cartRepo.findByBuyer_Id(util.userId);
        if(cart.isPresent()) {
            return cart.get();
        }
        return createNewCart();
    }

    @Override
    public Cart createNewCart(){
        Cart cart = Cart.builder()
                .cartItems(new ArrayList<>())
                .buyer(
                        buyerRepo.findById(util.userId)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                )
                .build();
        return cartRepo.save(cart);
    }
}
