package com.market.bridge.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.market.bridge.entity.order.OrderItem;
import com.market.bridge.entity.users.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "min_order")
    private Long minOrder;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "brand_name")
    private String brandName;


    @ManyToOne
    @JoinColumn(name = "Seller_id")
    private Seller seller;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductReview> productReviews;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;


    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDate modifiedAt;

//    public void addProductReview(ProductReview productReview) {
//        productReviews.add(productReview);
//        rating = (float) (productReviews.stream().map(p -> p.getRating()).collect(Collectors.toList()).stream()
//                        .mapToDouble(Float::floatValue).sum() / productReviews.size());
//    }

}
