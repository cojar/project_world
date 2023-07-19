package com.example.world.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductService {

    private ProductRepository productRepository;


    public Product create(String productName, String developer, String theme, Integer price, String content){
        Product product = new Product();

        product.setProductName(productName);
        product.setDeveloper(developer);
        product.setTheme(theme);
        product.setPrice(price);
        product.setContent(content);
        product.setCreateDate(LocalDate.now());
        this.productRepository.save(product);
        return product;
    }
}
