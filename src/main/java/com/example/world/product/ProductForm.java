package com.example.world.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

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
    private String device;
    // 상품 권장 장치 (콘솔 , pc )
    @Column(columnDefinition = "text")
    private String content;
    // 상품 내용

}
