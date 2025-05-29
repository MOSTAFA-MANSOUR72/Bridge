package com.market.bridge.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.market.bridge.entity.users.Buyer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "single_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "total_quantity")
    private Long totalQuantity;
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Buyer buyer;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;
}
