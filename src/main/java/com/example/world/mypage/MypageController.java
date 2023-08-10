package com.example.world.mypage;


import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.qna.Question;
import com.example.world.qna.QuestionService;
import com.example.world.review.Review;
import com.example.world.review.ReviewService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserForm;
import com.example.world.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final QuestionService questionService;
    private final ProductService productService;

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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/qna")
    public String myqna(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Question> paging = this.questionService.getList(page, size);
        List<Question> questionList = this.questionService.getQuestionList();
        List<Product> productList = this.productService.getProductList();
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("paging", paging);
        model.addAttribute("questionList", questionList);
        model.addAttribute("productList", productList);
        model.addAttribute("user", user);

        return "mypage/Mypage_qna";
    }
    @GetMapping("/review")
    public String myReview(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<Review> reviews = this.reviewService.getReviewsByAuthor(siteUser, page); // 수정된 부분

        model.addAttribute("reviews", reviews); // 리뷰 목록을 모델에 추가

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
