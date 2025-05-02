package com.market.bridge.service.Product;

import com.market.bridge.dto.product.ProductResponse;
import com.market.bridge.dto.product.ProductUpdateRequest;
import com.market.bridge.entity.Product;
import com.market.bridge.entity.users.Seller;
import com.market.bridge.mapper.ProductMapper;
import com.market.bridge.repository.ProductRepo;
import com.market.bridge.repository.SellerRepo;
import com.market.bridge.security.jwt.util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepo productRepo;
    private final SellerRepo sellerRepo;
    private final ProductMapper productMapper;

    @Override
    public String addProduct(Product product) {
        Seller seller = sellerRepo.findByUsername(util.username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        product.setBrandName(seller.getCompanyName());
        seller.addProduct(product);
        product.setSeller(seller);

        productRepo.save(product);
        sellerRepo.save(seller);
        return "Product added successfully";
    }

    @Override
    public void updateProduct(ProductUpdateRequest product, List<String> imagePaths) {
        Product existingProduct = productRepo.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.toProduct(existingProduct, product);
        existingProduct.setImages(imagePaths);
        productRepo.save(existingProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return  productRepo.findAll(pageable)
                .map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        return productRepo.findByCategoryId(categoryId, pageable)
                .map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsBySellerId(Long sellerId, Pageable pageable) {
        return productRepo.findBySellerId(sellerId, pageable)
                .map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByBrandName(String brandName, Pageable pageable) {
        return productRepo.findByBrandName(brandName, pageable)
                .map(productMapper::toProductResponse);
    }


}
