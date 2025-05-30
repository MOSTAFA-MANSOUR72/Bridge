package com.market.bridge.mapper;

import com.market.bridge.dto.product.ProductAddRequest;
import com.market.bridge.dto.product.ProductResponse;
import com.market.bridge.dto.product.ProductUpdateRequest;
import com.market.bridge.entity.Category;
import com.market.bridge.entity.Product;
import com.market.bridge.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepo categoryRepo;
    public Product toProduct(ProductAddRequest productRequest) {
        List<Long> categoriesIds = productRequest.getCategoriesIds();
        List<Category> categories = categoriesIds!= null? categoriesIds
                .stream()
                .map(u-> categoryRepo.findById(u)
                        .orElseThrow(() -> new RuntimeException("Category not found")))
                .collect(Collectors.toList()): null;
        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .categories(categories)
                .description(productRequest.getDescription())
                .minOrder(productRequest.getMinOrder())
                .rating(0f)
                .quantity(productRequest.getQuantity())
                .build();
    }

    public void toProduct(Product product, ProductUpdateRequest productUpdateRequest){
        List<Long> categoriesIds = productUpdateRequest.getCategoriesIds();
        List<Category> categories = categoriesIds!= null? categoriesIds
                .stream()
                .map(u-> categoryRepo.findById(u)
                        .orElseThrow(() -> new RuntimeException("Category not found")))
                .collect(Collectors.toList()): new ArrayList<>();
                product.setName(productUpdateRequest.getName());
                product.setDescription(productUpdateRequest.getDescription());
                product.setPrice(productUpdateRequest.getPrice());
                product.setMinOrder(productUpdateRequest.getMinOrder());
                product.setQuantity(productUpdateRequest.getQuantity());
                product.setCategories(categories);
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .minOrder(product.getMinOrder())
                .quantity(product.getQuantity())
                .rating(product.getRating())
                .images(product.getImages())
                .categoriesIdes(product.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                .brandName(product.getBrandName())
                .sellerId(product.getSeller().getId())
                .build();
    }
}
