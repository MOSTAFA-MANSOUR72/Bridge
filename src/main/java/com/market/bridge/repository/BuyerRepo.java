package com.market.bridge.repository;

import com.market.bridge.entity.users.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepo extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByUsername(String username);
    Optional<Buyer> findBuyerByEmail(String email);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
