package com.market.bridge.repository;

import com.market.bridge.entity.order.SingleOrder;
import com.market.bridge.entity.users.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<SingleOrder, Long> {
    Page<SingleOrder> findByBuyerOrderByCreatedAtDesc(Buyer buyer, Pageable pageable);
}
