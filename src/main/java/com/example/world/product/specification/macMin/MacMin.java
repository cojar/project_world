package com.example.world.product.specification.macMin;

import com.example.world.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MacMin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operatingSystem;
    //운영체제
    private String processor;
    //프로세서
    private String memory;
    //RAM
    private String graphics;
    //그래픽카드
    private String Storage;
    //저장 공간
    private String directAccess;
    //DirectX
    private String network;
    //인터넷 연결
    @ManyToOne
    private Product product;

}
