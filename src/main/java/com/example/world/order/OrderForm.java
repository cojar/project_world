package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ToString
public class OrderForm {

    private Product product;

    private SiteUser user;

    @NotEmpty(message = "구매자명은 필수 입력항목입니다.")
    private String customerName;

    @NotEmpty(message = "이메일은 필수 입력항목입니다.")
    private String email;

    @NotEmpty(message = "지불방식은 필수 선택항목입니다.")
    private String payment;

}
