package com.market.bridge.repository;

import com.market.bridge.entity.users.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuyerRepo extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByUsername(String username);
}
