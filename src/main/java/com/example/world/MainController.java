package com.example.world;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/hi")
    public String hi(){
        return "navbar";
    }
}

