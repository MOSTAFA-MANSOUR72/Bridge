package com.market.bridge.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAddRequest {
    private String name;
    private String description;
    private Double price;
    private Long minOrder;
    private Long quantity;
    private List<Long> categoriesIds;
    private List<MultipartFile> images;
}
