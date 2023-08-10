package com.example.world.product.productImage;

import com.example.world.file.FileService;
import com.example.world.file.UploadedFile;
import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileService fileService;

    public ProductImage create(String name, MultipartFile file, Product product) throws IOException {
        ProductImage productImage = new ProductImage();

        UploadedFile image = this.fileService.upload(file, "product", "productImage", String.valueOf(product.getId()));


        productImage.setName(name);
        productImage.setImage(image);
        productImage.setProduct(product);

        this.productImageRepository.save(productImage);
        return productImage;
    }

    public List<ProductImage> getList() {
        return this.productImageRepository.findAll();
    }
}