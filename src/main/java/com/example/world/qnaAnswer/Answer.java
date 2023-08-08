package com.example.world.qnaAnswer;


import com.example.world.qna.Question;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //질문답변 번호

    @Column(columnDefinition = "text")
    private String content;
    //질문답변 내용

    private LocalDateTime createDate;

    @OneToOne
    private Question question;
    // 답변과 연결된 질문

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

}
