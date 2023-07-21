package com.example.world.product.specification.windowRecommended;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WindowRecommendedForm {
    @NotEmpty
    private String operatingSystem;
    //운영체제
    @NotEmpty
    private String processor;
    //프로세서
    @NotEmpty
    private String memory;
    //RAM
    @NotEmpty
    private String graphics;
    //그래픽카드
    @NotEmpty
    private String storage;
    //저장 공간
    private String directAccess;
    //DirectX
    private String network;
    //인터넷 연결
}
