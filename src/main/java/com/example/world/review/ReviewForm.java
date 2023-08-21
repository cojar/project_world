package com.example.world.review;

import com.example.world.order.ProductOrder;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {

    private int score;

    @NotEmpty(message = "내용은 필수항목입니다")
    private String content;

    private Long productOrderId;


}
