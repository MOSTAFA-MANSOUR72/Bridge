package com.market.bridge.repository;

import com.market.bridge.entity.users.Buyer;
import com.market.bridge.entity.users.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUsername(String username);
    Optional<Seller> findSellerByEmail(String email);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
