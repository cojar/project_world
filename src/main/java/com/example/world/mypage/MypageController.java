package com.example.world.mypage;


import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.qna.Question;
import com.example.world.qna.QuestionService;
import com.example.world.review.Review;
import com.example.world.review.ReviewService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserForm;
import com.example.world.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    @Autowired
    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final QuestionService questionService;

    @GetMapping("")
    public String mypageMain(Model model , Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<ProductOrder> articles = this.orderService.getAuthor(siteUser);
        List<Review> reviews = this.reviewService.getAuthor(siteUser);
        List<Question> questions = this.questionService.getAuthor(siteUser);

        int orderCount = articles.size();
        int reviewCount = reviews.size();
        int questionCount=questions.size();

        model.addAttribute("questionCount",questionCount);
        model.addAttribute("reviewCount",reviewCount);
        model.addAttribute("orderCount",orderCount);
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
    public String myReview(){
        return "/mypage/Mypage_review";
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public String myStatus(UserForm userForm, Model model , Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<ProductOrder> articles = this.orderService.getAuthor(siteUser);
        List<Review> reviews = this.reviewService.getAuthor(siteUser);
        List<Question> questions = this.questionService.getAuthor(siteUser);

        int orderCount = articles.size();
        int reviewCount = reviews.size();
        int questionCount=questions.size();

        model.addAttribute("questionCount",questionCount);
        model.addAttribute("reviewCount",reviewCount);
        model.addAttribute("orderCount",orderCount);
        model.addAttribute("user",siteUser);
        return "/mypage/Mypage_usr";
    }


    @PostMapping("/user/modify")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String userEdit(UserForm userForm, Principal principal, BindingResult bindingResult) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            return "다시 시도해 주세요. ";
        }else if (userForm.getPassword1().equals("")) {
            return "변경할 비밀번호를 입력해주세요";
        }else if (userForm.getNickname().equals("")) {
            return "변경할 닉네임을 입력해주세요";
        }else if (!userForm.getPassword1().equals(userForm.getPassword2())) {
            return "비밀번호 2개가 일치하지 않습니다.";
        }else if (userForm.getNickname().equals(siteUser.getNickname())) {
            return "같은 닉네임으로는 변경이 불가능합니다.";
        }else {
            this.userService.modifyUser(siteUser,userForm.getNickname(),userForm.getPassword1());
            return "성공적으로 수정되었습니다.";
        }
    }

}
