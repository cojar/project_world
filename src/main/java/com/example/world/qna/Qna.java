package com.example.world.qna;

import com.example.world.product.Product;
import com.example.world.productReview.ProductReview;
import com.example.world.qnaAnswer.QnaAnswer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //질문 번호

    private LocalDate createDate;

    @Size(max = 30)
    private String subject;
    // 질문 제목

    @Column(columnDefinition = "text")
    private String content;
    //질문 내용

    @ManyToOne
    private Product product;
    // 질문과 연결된 상품


    @OneToMany(mappedBy = "qna", cascade = CascadeType.REMOVE)
    private List<QnaAnswer> answerList;
}
