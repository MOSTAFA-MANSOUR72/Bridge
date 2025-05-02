package com.market.bridge.controller;

import com.market.bridge.dto.product.ProductAddRequest;
import com.market.bridge.dto.product.ProductResponse;
import com.market.bridge.dto.product.ProductUpdateRequest;
import com.market.bridge.entity.Product;
import com.market.bridge.mapper.ProductMapper;
import com.market.bridge.service.Product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createProduct(@ModelAttribute ProductAddRequest productRequest) {
        try {
            List<MultipartFile> images = productRequest.getImages();
            List<String> imagePaths = saveImages(images);
            Product product = productMapper.toProduct(productRequest);
            product.setImages(imagePaths);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.addProduct(product));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductUpdateRequest productRequest) {
        try {

            List<MultipartFile> images = productRequest.getImages();
            List<String> imagePaths = saveImages(images);
            productService.updateProduct(productRequest, imagePaths);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
    public List<String> saveImages(List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();
        for(MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            File file = new File(uploadDir + fileName);
            try {
                // Save the file in the images directory
                image.transferTo(file);
                imagePaths.add(file.getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException("Error uploading file: " + fileName + " " + e.getMessage());
            }
        }
        return imagePaths;
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                            @RequestParam(defaultValue = "50") int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts(pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long categoryId,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "50") int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductsByCategoryId(categoryId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try{
            productService.deleteProductById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


}