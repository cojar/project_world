package com.example.world.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class UserForm {

    private String nickname;

    private String password1;
    private String password2;

}
