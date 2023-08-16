package com.example.world.review;

import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.qna.Question;
import com.example.world.qna.QuestionForm;
import com.example.world.qnaAnswer.AnswerForm;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/review")
@RequiredArgsConstructor // 변수를 포함하는 생성자를 자동으로 생성.
@Controller
public class ReviewController {
    private final OrderService orderService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final ProductService productService;


    @GetMapping("/list/{productId}")
    public String list(Model model,
                       @PathVariable("productId") Long productId,
                       @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Review> paging = this.reviewService.getListByProductId(productId, page - 1);
        model.addAttribute("paging", paging);
        return "review_list";
    }

//    @GetMapping("/orderCheck")
//    public ResponseEntity<Map<String, Boolean>> checkOrderValidity(@RequestParam Long orderId) {
//        boolean isValid = orderService.isOrderValid(orderId);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("valid", isValid);
//        return ResponseEntity.ok(response);
//    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Review review = this.reviewService.getReview(id);
        model.addAttribute("review", review);
        return "review_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String reviewCreate(@RequestParam("productOrderId") Long productOrderId,
                               @RequestParam("userId") Long userId,
                               @RequestParam("productId") Long productId,
            Model model) {

        ProductOrder productOrder = orderService.getOrder(productOrderId);
        SiteUser author = userService.getUser(userId);

        model.addAttribute("productOrderId", productOrderId);
        model.addAttribute("productId", productId);

        return "review_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String reviewCreate(@Valid ReviewForm reviewForm,
                               BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "review_form";
        }

        Long productOrderId = reviewForm.getProductOrderId();
        try {
            ProductOrder productOrder = orderService.getOrder(productOrderId);

            SiteUser siteUser = userService.getUser(principal.getName());
            reviewService.create(reviewForm.getContent(), siteUser, productOrder);
            return "redirect:/review/list";
        } catch (IllegalArgumentException e) {
            return "redirect:/product/list/all"; // 주문이 유효하지 않은 경우 처리
        }
    }


//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/create/{id}")
//    public String createReview(@PathVariable("id") Long id, @Valid ReviewForm reviewForm,
//                               BindingResult bindingResult, Principal principal,
//                               Model model) {
//        ProductOrder productOrder = this.orderService.getOrder(id);
//        if (bindingResult.hasErrors()) {
//            return "review_form";
//        }
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.reviewService.create(productOrder, reviewForm.getContent(), siteUser);
//        return "redirect:/review/list";
//    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String reviewModify(ReviewForm reviewForm, @PathVariable("id") Long id, Principal principal) {
        Review review = this.reviewService.getReview(id);
        if(!review.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        reviewForm.setContent(review.getContent());
        return "review_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String reviewModify(@Valid ReviewForm reviewForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "review_form";
        }
        Review review = this.reviewService.getReview(id);
        if (!review.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.reviewService.modify(review, reviewForm.getContent());
        return String.format("redirect:/review/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String reviewDelete(Principal principal, @PathVariable("id") Long id) {
        Review review = this.reviewService.getReview(id);
        if (!review.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.reviewService.delete(review);
        return "redirect:/product/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String reviewVote(@PathVariable("id") Long id,
                             Principal principal) {
        Review review = this.reviewService.getReview(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.reviewService.vote(review, siteUser);
        return String.format("redirect:/review/detail/%s#answer_%s", review.getProductOrder().getId(), review.getId());
    }
}
