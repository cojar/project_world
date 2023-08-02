package com.example.world.product;

import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String productName;
    // 게임 이름
    @NotEmpty
    private String developer;
    // 게임 개발사
    @NotEmpty
    private String theme;
    //게임 테마 (장르)
    @NotEmpty
    private String price;
    // 상품 가격
    private List<MacMinForm> macMinList;
    // 맥 최소 스펙
    private List<MacRecommendedForm> macRecommendedList;
    // 맥 권장 스펙
    private List<WindowMinForm> windowMinList;
    // 맥 최소 스펙
    private List<WindowRecommendedForm> windowRecommendedList;
    // 맥 권장 스펙
    @Column(columnDefinition = "text")
    private String content;
    // 상품 설명
}
