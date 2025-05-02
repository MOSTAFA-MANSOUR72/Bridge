package com.market.bridge.service.Product;

import com.market.bridge.dto.product.ProductAddRequest;
import com.market.bridge.dto.product.ProductResponse;
import com.market.bridge.dto.product.ProductUpdateRequest;
import com.market.bridge.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    String addProduct(Product product);
    void updateProduct(ProductUpdateRequest product, List<String> imagePaths);
    void deleteProductById(Long id);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> getProductsByCategoryId(Long categoryId, Pageable pageable);
    Page<ProductResponse> getProductsBySellerId(Long sellerId, Pageable pageable);
    Page<ProductResponse> getProductsByBrandName(String brandName, Pageable pageable);
}
