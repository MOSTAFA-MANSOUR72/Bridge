package com.market.bridge.entity.cart;

import com.market.bridge.entity.users.Buyer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Buyer buyer;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    public void addCartItem(CartItem item) {
        if(cartItems == null) {
            cartItems = new ArrayList<>();
        }
        cartItems.add(item);
    }
}

