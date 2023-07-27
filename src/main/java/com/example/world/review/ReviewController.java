package com.example.world.review;

import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/review")
@RequiredArgsConstructor
@Controller
public class ReviewController {

    private UserService userService;
    private ProductService productService;
    private ReviewService reviewService;

//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
//    public String createReview(@PathVariable("id") Long id, @Valid ProductReviewForm productReviewForm,
//                               BindingResult bindingResult, Principal principal, Model model){
    public String createReview(@PathVariable("id") Long id, @Valid ReviewForm reviewForm,
                               BindingResult bindingResult, Model model){
        Product product = this.productService.getProduct(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("product",product);
            return  "product_detail";
        }
//        System.out.println(productReviewForm);
//        ProductReview productReview = this.productReviewService.create(product, productReviewForm.getContent(), siteUser);
        Review review = this.reviewService.create(product, reviewForm.getContent());
        return String.format("redirect:/product/detail/%s#review_%s", review.getProduct().getId(), review.getId());
    }

}

//        System.out.println(reviewForm);
//        Review review = this.reviewService.create(product, reviewForm.getContent(), siteUser);
//        return String.format("redirect:/product/detail/%s#review_%s", review.getProduct().getId(), review.getId());
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/modify/{id}")
//    public String reviewModify(@PathVariable("id") Long id,
//                               ReviewForm reviewForm,
//                               Principal principal,
//                               Model model) {
//        Review review = this.reviewService.getReview(id);
//        if (!review.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        reviewForm.setContent(review.getContent());
//
//        // 리뷰 목록과 리뷰 폼을 다시 전달
//        model.addAttribute("reviews", reviewService.getList()); // 리뷰 목록 데이터
//        model.addAttribute("reviewForm", new ReviewForm()); // 리뷰 폼 초기화
//
//        return String.format("redirect:/product/detail/%s#review_%s", review.getProduct().getId(), review.getId());
//    }
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/modify/{id}")
//    public String reviewModify(@Valid ReviewForm reviewForm, @PathVariable("id") Long id,
//                               BindingResult bindingResult, Principal principal) {
//        if (bindingResult.hasErrors()) {
//            return "review_form";
//        }
//        Review review = this.reviewService.getReview(id);
//        if (!review.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        this.reviewService.modify(review, reviewForm.getContent());
//        return String.format("redirect:/product/detail/%s#review_%s", review.getProduct().getId(), review.getId());
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/delete/{id}")
//    public String reviewDelete(Principal principal, @PathVariable("id") Long id) {
//        Review review = this.reviewService.getReview(id);
//        if (!review.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
//        this.reviewService.delete(review);
//        return String.format("redirect:/product/detail/%s", review.getProduct().getId());
//    }
//
//
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/vote/{id}")
//    public String reviewVote(@PathVariable("id") Long id,
//                             Principal principal) {
//        Review review = this.reviewService.getReview(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.reviewService.vote(review, siteUser);
//        return String.format("redirect:/question/detail/%s#answer_%s", review.getProduct().getId(), review.getId());
//    }
//}