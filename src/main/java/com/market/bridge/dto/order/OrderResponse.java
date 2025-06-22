package com.market.bridge.dto.order;

import com.market.bridge.entity.Address;
import com.market.bridge.entity.enums.OrderStatus;
import com.market.bridge.entity.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Double totalPrice;
    private Long totalQuantity;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private String paymentMethod;
    private LocalDate estimatedDeliveryDate;
    private LocalDate createdAt;
    private Address shippingAddress;
}