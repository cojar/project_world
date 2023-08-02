package com.example.world.review;

import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //리뷰 번호

    @Column(columnDefinition = "text")
    private String content;
    //리뷰 내용

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private Product product;
    // 리뷰와 연결된 상품

    @ManyToOne
    private SiteUser author;
    @ManyToMany
    Set<SiteUser> voter;

    private int score;
    // 해당 게임 평점
}
