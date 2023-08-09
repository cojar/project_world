package com.example.world.review;

import com.example.world.DataNotFoundException;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.qna.Question;
import com.example.world.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final OrderService orderService;

    public Page<Review> getListByProductId(Long productId, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return reviewRepository.findByProductOrder_Product_Id(productId, pageable);
    }


    public Review create(String content, SiteUser user, ProductOrder productOrder) {
        Review review = new Review();
        review.setContent(content);
        review.setCreateDate(LocalDateTime.now());
        review.setProductOrder(productOrder);
        review.setAuthor(user);
        this.reviewRepository.save(review);
        return review;
    }

    public Review getReview(Long id) {
        Optional<Review> review = this.reviewRepository.findById(id);
        if (review.isPresent()) {
            return review.get();
        } else {
            throw new DataNotFoundException("review not found");
        }
    }

    public List<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    public void modify(Review review, String content) {
        review.setContent(content);
        review.setModifyDate(LocalDateTime.now());
        this.reviewRepository.save(review);
    }

    public void vote(Review review, SiteUser siteUser) {
        review.getVoter().add(siteUser);
        this.reviewRepository.save(review);
    }

    public void delete(Review review) {
        this.reviewRepository.delete(review);
    }

    public List<Review> getReviewsByAuthor(SiteUser author) {
        return reviewRepository.findByAuthor(author);
    }

}


