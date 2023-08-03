package com.example.world.admin;

import com.example.world.notice.Notice;
import com.example.world.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final NoticeService noticeService;

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
    public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "admin/admin_notice";
    }
}
