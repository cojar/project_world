package com.example.world.review;

import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/review")
@RequiredArgsConstructor // 변수를 포함하는 생성자를 자동으로 생성.
@Controller
public class ReviewController {
    private final OrderService orderService;
    private final UserService userService;
    private final ReviewService reviewService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="1") int page) {
        Page<Review> paging = this.reviewService.getList(page-1);
        model.addAttribute("paging", paging);
        return "review_list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createReview(@PathVariable("id") Long id, @Valid ReviewForm reviewForm,
                               BindingResult bindingResult, Principal principal,
                               Model model) {
        ProductOrder productOrder = this.orderService.getOrder(id);
        if (bindingResult.hasErrors()) {
            return "review_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.reviewService.create(productOrder, reviewForm.getContent(), siteUser);
        return "redirect:/review/list";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String reviewModify(@PathVariable("id") Long id,
                               ReviewForm reviewForm,
                               Principal principal) {
        Review review = this.reviewService.getReview(id);
        if(!review.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        reviewForm.setContent(review.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String reviewModify(@Valid ReviewForm reviewForm, @PathVariable("id") Long id,
                               BindingResult bindingResult, Principal principal) {
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
        return String.format("redirect:/productOrder/detail/%s", review.getProductOrder().getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String reviewVote(@PathVariable("id") Long id,
                             Principal principal) {
        Review review = this.reviewService.getReview(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.reviewService.vote(review, siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s", review.getProductOrder().getId(), review.getId());
    }
}
