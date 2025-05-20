package com.market.bridge.repository;

import com.market.bridge.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.buyer.id = :buyerId")
    Optional<Cart> findByBuyer_Id(Long buyerId);
}
