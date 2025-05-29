package com.market.bridge.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.market.bridge.entity.users.Buyer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "product_review")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductReview {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @JsonBackReference
    private Buyer buyer;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private float rating;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDate modifiedAt;
}
