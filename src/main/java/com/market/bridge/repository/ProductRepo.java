package com.market.bridge.repository;

import com.market.bridge.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("Select p from Product p join p.categories c where c.id = :categoryId")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("Select p from Product p join Seller s on s.id = p.id where s.id = :sellerId")
    Page<Product> findBySellerId(Long sellerId, Pageable pageable);

    Page<Product> findByBrandName(String brandName, Pageable pageable);
}
