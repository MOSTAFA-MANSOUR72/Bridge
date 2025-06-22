package com.market.bridge.dto.order;

import com.market.bridge.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAddRequest {
    private String accountNumber; // For payment processing
    private Address shippingAddress;
    private Boolean CashOnDelivery; // If true, use don't use paymentCode, otherwise use it for payment
} 