package com.example.world.review;

import com.example.world.DataNotFoundException;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.qna.Question;
import com.example.world.qnaAnswer.Answer;
import com.example.world.user.SiteUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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


    public Review create(int score, String content, SiteUser author, ProductOrder productOrder) {

        Review review = new Review();

        review.setScore(score);
        review.setContent(content);
        review.setAuthor(author);
        review.setProductOrder(productOrder);
        review.setCreateDate(LocalDateTime.now());


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

    public Page<Review> getList(int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.reviewRepository.findAll(pageable);
    }

    public List<Review> getReviewList() {
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

    public void cancelVote(Review review, SiteUser siteUser) {
        review.getVoter().remove(siteUser);
        this.reviewRepository.save(review);
    }

    public void delete(Review review) {
        this.reviewRepository.delete(review);
    }

    public List<Review> getAuthor(SiteUser siteUser) {
        return this.reviewRepository.findByAuthor(siteUser);
    }

    public Page<Review> getReviewsByAuthor(SiteUser siteUser, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));
        return this.reviewRepository.findByAuthor(siteUser,pageable);

    }

    public Page<Review> getReviewsByAuthor(SiteUser author, int page, int pageSize) {
        return this.reviewRepository.findByAuthor(author, PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id"))));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + reviewId));

        reviewRepository.delete(review);
    }



    public Specification<SiteUser> searchUser(SiteUser sortkey){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Season 컬럼을 기준으로 검색 조건 생성
            if (sortkey != null) {
                Path<String> seasonPath = root.get("author");
                Predicate seasonPredicate = criteriaBuilder.equal(seasonPath, sortkey);
                predicates.add(seasonPredicate);
            }

            // 다른 조건들을 추가하고 싶다면 여기에 추가

            // 검색 조건들을 조합하여 최종 검색 조건 생성
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


