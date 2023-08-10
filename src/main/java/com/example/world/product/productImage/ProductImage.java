package com.example.world.product.productImage;

import com.example.world.file.UploadedFile;
import com.example.world.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @OneToOne
    private UploadedFile image;

    @ManyToOne
    private Product product;
}
