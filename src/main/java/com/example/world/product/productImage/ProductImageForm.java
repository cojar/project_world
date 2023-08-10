package com.example.world.product.productImage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductImageForm {
    private String name;

    private MultipartFile image;
}
