package com.example.world.qnaAnswer;


import com.example.world.product.Product;
import com.example.world.qna.Qna;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class QnaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //질문답변 번호

    private LocalDate createDate;


    @Column(columnDefinition = "text")
    private String content;
    //질문답변 내용

    @ManyToOne
    private Qna qna;
    // 답변과 연결된 질문
}
