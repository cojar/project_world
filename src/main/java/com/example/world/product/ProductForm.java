package com.example.world.product;

import com.example.world.product.operatingsystem.OperatingSystemForm;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    @NotEmpty
    private String name;
    // 게임 이름
    @NotEmpty
    private String developer;
    // 게임 개발사
    @NotEmpty
    private int price;
    // 상품 가격
    @NotEmpty
    private String thema;
    //게임 테마 (장르)

    @NotEmpty
    private List<OperatingSystemForm> operatingSystemList;
    // 운영체제

    @NotEmpty
    private List<SpecificationForm> specificationList;
    // 스펙

    @Column(columnDefinition = "text")
    private String content;
    // 상품 설명

}
