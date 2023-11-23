package com.example.world.qna;

import com.example.world.product.Product;
import com.example.world.qnaAnswer.Answer;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //질문 번호

    @Column(columnDefinition = "text")
    private String content;
    //질문 내용

    private LocalDateTime createDate;

    @ManyToOne
    private Product product;
    // 질문과 연결된 상품

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    private boolean answered;
}
