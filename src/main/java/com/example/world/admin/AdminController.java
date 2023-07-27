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

    @GetMapping("/ad/user")
    public String adminUser(){
        return "admin/admin_user";
    }

    @GetMapping("ad/review")
    public String adminReview(){
        return "admin/admin_review";
    }
    @GetMapping("ad/qna")
    public String adminQna(){
        return "admin/admin_qna";
    }

    @GetMapping("ad/notice")
    public String adminNotice(){
        return "admin/admin_notice";
    }
}
