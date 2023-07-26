package com.example.world.productReview;

import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //리뷰 번호

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Size(max = 30)
    private String subject;
    // 리뷰 제목

    @Column(columnDefinition = "text")
    private String content;
    //리뷰 내용

    @ManyToOne
    private Product product;
    // 리뷰와 연결된 상품

    @ManyToOne
    private SiteUser siteUser;
    @ManyToMany
    Set<SiteUser> voter;

    private int score;
    // 해당 게임 평점
}
