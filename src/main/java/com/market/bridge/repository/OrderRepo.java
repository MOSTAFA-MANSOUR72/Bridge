package com.market.bridge.repository;

import com.market.bridge.entity.order.SingleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<SingleOrder, Long> {

}
