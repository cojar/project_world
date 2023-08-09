package com.example.world.mypage;



import com.example.world.review.Review;
import com.example.world.review.ReviewService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;

    private final ReviewService reviewService;

    @GetMapping("")
    public String mypageMain(Model model , Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        model.addAttribute("user",siteUser);
        return "/mypage/Mypage_main";
    }

    @GetMapping("/order")
    public String myOrder(){
        return "/mypage/Mypage_order";
    }
    @GetMapping("/qna")
    public String myqna(){
        return "/mypage/Mypage_qna";
    }

    @GetMapping("/review")
    public String myReview(Model model, Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<Review> reviewList = this.reviewService.getReviewsByAuthor(siteUser);
        model.addAttribute("user",siteUser);
        model.addAttribute("review",reviewList);

        return "/mypage/Mypage_review";
    }

    @GetMapping("/user")
    public String myStatus(){
        return "/mypage/Mypage_usr";
    }
}
