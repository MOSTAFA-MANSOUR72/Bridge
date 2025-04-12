package com.market.bridge.repository;

import com.market.bridge.entity.users.Admin;
import com.market.bridge.entity.users.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
