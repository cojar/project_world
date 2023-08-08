package com.example.world.qna;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    private Long productId;
}
