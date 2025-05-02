package com.market.bridge.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long minOrder;
    private Long quantity;
    private List<Long> categoriesIds;
    private List<MultipartFile> images;
}
