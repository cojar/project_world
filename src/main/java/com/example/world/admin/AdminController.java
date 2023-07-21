package com.example.world.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/ad")
    public  String adminMain(){
        return "admin/admin_main";
    }

    @GetMapping("/ad/order")
    public String adminOrder(){
        return "admin/admin_order";
    }

    @GetMapping("/ad/product")
    public String adminProduct(){
        return "admin/admin_product";
    }
}
