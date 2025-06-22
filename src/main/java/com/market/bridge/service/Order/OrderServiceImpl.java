package com.market.bridge.service.Order;

import com.market.bridge.dto.order.OrderAddRequest;
import com.market.bridge.dto.order.OrderResponse;
import com.market.bridge.entity.cart.Cart;
import com.market.bridge.entity.cart.CartItem;
import com.market.bridge.entity.enums.OrderStatus;
import com.market.bridge.entity.order.OrderItem;
import com.market.bridge.entity.order.SingleOrder;
import com.market.bridge.entity.users.Buyer;
import com.market.bridge.repository.BuyerRepo;
import com.market.bridge.repository.CartItemRepo;
import com.market.bridge.repository.OrderItemRepo;
import com.market.bridge.repository.OrderRepo;
import com.market.bridge.security.jwt.util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final BuyerRepo buyerRepo;
    private final CartItemRepo cartItemRepo;
    private final RestTemplate restTemplate;

    @Override
    public OrderResponse createOrder(OrderAddRequest orderAddRequest) {
        try {
            // Get current buyer
            Buyer buyer = buyerRepo.findById(util.userId)
                    .orElseThrow(() -> new RuntimeException("Buyer not found"));

            // Get buyer's cart
            Cart cart = buyer.getCart();
            if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            List<CartItem> cartItems = cart.getCartItems();

            // Calculate total amount
            Double totalAmount = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();

            // Process payment if not cash on delivery
            if (!orderAddRequest.getCashOnDelivery()) {
                if (orderAddRequest.getAccountNumber() == null || orderAddRequest.getAccountNumber().isEmpty()) {
                    throw new RuntimeException("Payment code is required for online payment");
                }

                boolean paymentSuccess = processPayment(orderAddRequest.getAccountNumber(), totalAmount);
                if (!paymentSuccess) {
                    throw new RuntimeException("Payment failed. Please check your payment details and try again.");
                }
            }

            // Create order items from cart items
            List<OrderItem> orderItems = cartItems.stream()
                    .map(cartItem -> {
                        OrderItem orderItem = OrderItem.builder()
                                .product(cartItem.getProduct())
                                .quantity(cartItem.getQuantity())
                                .totalPrice(cartItem.getTotalPrice())
                                .build();
                        return orderItemRepo.save(orderItem);
                    })
                    .collect(Collectors.toList());

            // Create the order
            SingleOrder order = SingleOrder.builder()
                    .totalPrice(totalAmount)
                    .totalQuantity(cartItems.stream().mapToLong(CartItem::getQuantity).sum())
                    .status(orderAddRequest.getCashOnDelivery() ? OrderStatus.PENDING : OrderStatus.PROCESSING)
                    .orderItems(orderItems)
                    .buyer(buyer)
                    .paymentMethod(orderAddRequest.getCashOnDelivery() ? "Cash on Delivery" : "Online Payment")
                    .estimatedDeliveryDate(LocalDate.now().plusDays(7))
                    .createdAt(LocalDate.now())
                    .build();

            // Save the order
            SingleOrder savedOrder = orderRepo.save(order);

            // Set the order reference for each order item
            orderItems.forEach(item -> {
                item.setOrder(savedOrder);
                orderItemRepo.save(item);
            });

            // Clear the cart after successful order creation
            cartItems.forEach(cartItemRepo::delete);
            cart.setCartItems(new ArrayList<>());

            return OrderResponse.builder()
                    .id(savedOrder.getId())
                    .totalPrice(savedOrder.getTotalPrice())
                    .totalQuantity(savedOrder.getTotalQuantity())
                    .status(savedOrder.getStatus())
                    .orderItems(savedOrder.getOrderItems())
                    .paymentMethod(savedOrder.getPaymentMethod())
                    .estimatedDeliveryDate(savedOrder.getEstimatedDeliveryDate())
                    .createdAt(savedOrder.getCreatedAt())
                    .shippingAddress(orderAddRequest.getShippingAddress())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    @Override
    public String cancelOrder(Long orderId) {
        try {
            SingleOrder order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Check if order belongs to current user
            if (!order.getBuyer().getId().equals(util.userId)) {
                throw new RuntimeException("You can only cancel your own orders");
            }

            // Check if order can be cancelled
            if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
                throw new RuntimeException("Cannot cancel order that is already shipped or delivered");
            }

            order.setStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);

            return "Order cancelled successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel order: " + e.getMessage());
        }
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        try {
            SingleOrder order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Check if order belongs to current user
            if (!order.getBuyer().getId().equals(util.userId)) {
                throw new RuntimeException("You can only view your own orders");
            }

            return OrderResponse.builder()
                    .id(order.getId())
                    .totalPrice(order.getTotalPrice())
                    .totalQuantity(order.getTotalQuantity())
                    .status(order.getStatus())
                    .orderItems(order.getOrderItems())
                    .paymentMethod(order.getPaymentMethod())
                    .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                    .createdAt(order.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order: " + e.getMessage());
        }
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        try {
            Buyer buyer = buyerRepo.findById(util.userId)
                    .orElseThrow(() -> new RuntimeException("Buyer not found"));

            Page<SingleOrder> orders = orderRepo.findByBuyerOrderByCreatedAtDesc(buyer, pageable);

            return orders.map(order -> OrderResponse.builder()
                    .id(order.getId())
                    .totalPrice(order.getTotalPrice())
                    .totalQuantity(order.getTotalQuantity())
                    .status(order.getStatus())
                    .orderItems(order.getOrderItems())
                    .paymentMethod(order.getPaymentMethod())
                    .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                    .createdAt(order.getCreatedAt())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get orders: " + e.getMessage());
        }
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        try {
            SingleOrder order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Check if order belongs to current user
            if (!order.getBuyer().getId().equals(util.userId)) {
                throw new RuntimeException("You can only update your own orders");
            }

            OrderStatus newStatus;
            try {
                newStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid order status: " + status);
            }

            order.setStatus(newStatus);
            SingleOrder updatedOrder = orderRepo.save(order);

            return OrderResponse.builder()
                    .id(updatedOrder.getId())
                    .totalPrice(updatedOrder.getTotalPrice())
                    .totalQuantity(updatedOrder.getTotalQuantity())
                    .status(updatedOrder.getStatus())
                    .orderItems(updatedOrder.getOrderItems())
                    .paymentMethod(updatedOrder.getPaymentMethod())
                    .estimatedDeliveryDate(updatedOrder.getEstimatedDeliveryDate())
                    .createdAt(updatedOrder.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }

    /**
     * Process payment using the banking API
     * @param paymentCode Account number or payment code
     * @param amount Amount to debit
     * @return true if payment successful, false otherwise
     */
    private boolean processPayment(String paymentCode, Double amount) {
        try {
            String url = "http://localhost:8000/api/transaction/debit";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("accountNumber", paymentCode);
            requestBody.put("amount", amount);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Boolean> response = restTemplate.postForEntity(url, request, Boolean.class);
            
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.err.println("Payment processing failed: " + e.getMessage());
            return false;
        }
    }
}
