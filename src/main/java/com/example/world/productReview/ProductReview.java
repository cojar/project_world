package com.example.world.productReview;

import com.example.world.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //리뷰 번호

    private LocalDate createDate;

    @Size(max = 30)
    private String subject;
    // 리뷰 제목

    @Column(columnDefinition = "text")
    private String content;
    //리뷰 내용

    @ManyToOne
    private Product product;
    // 리뷰와 연결된 상품

    private int score;
    // 해당 게임 평점
}
