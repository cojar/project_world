package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.review.Review;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser user;

    @Column(length = 200)
    private String customerName;

    @Column(length = 200)
    private String email;

    @Column(length = 200)
    private String payment;

    @Column(length = 200)
    private String orderStatus = "주문완료";

    @Column(length = 200)
    private String code;


    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;


    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;
    // 상품과 연결된 리뷰들
}
