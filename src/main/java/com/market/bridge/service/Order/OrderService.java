package com.market.bridge.service.Order;

import com.market.bridge.dto.order.OrderAddRequest;
import com.market.bridge.dto.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderAddRequest orderAddRequest);
    String cancelOrder(Long orderId);
    OrderResponse getOrderById(Long orderId);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse updateOrderStatus(Long orderId, String status);
}
