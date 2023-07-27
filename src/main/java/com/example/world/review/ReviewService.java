package com.example.world.review;

import com.example.world.DataNotFoundException;
import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private ReviewRepository reviewRepository;

    public List<Review> getList(){
        return this.reviewRepository.findAll();
    }
//    public ProductReview create(Product product, String content, SiteUser author){
    public Review create(Product product, String content){
        Review review = new Review();
        review.setContent(content);
        review.setCreateDate(LocalDateTime.now());
        review.setProduct(product);
//        productReview.setAuthor(author);
        this.reviewRepository.save(review);
        return review;

    }

    public Review getReview(Long id){
        Optional<Review> review = this.reviewRepository.findById(id);
        if (review.isPresent()) {
            return review.get();
        } else {
            throw new DataNotFoundException("review not found"); // 예외처리로 에러(DataNotFoundException)를 표시
        }
    }

//    public List<ProductReview> getReviewsByAuthor(SiteUser author) {
//        return productReviewRepository.findByAuthor(author);
//    }


}

//
//    public Review getReview(Long id) {// Integer 로 타입이 들어오면 null 값도 허용해줄 수 있음
//        Optional<Review> review = this.reviewRepository.findById(id);
//        if (review.isPresent()) {
//            return review.get();
//        } else {
//            throw new DataNotFoundException("review not found"); // 예외처리로 에러(DataNotFoundException)를 표시
//        }
//    }
//    public List<Review> getReviewsByAuthor(SiteUser author) {
//        return reviewRepository.findByAuthor(author);
//    }
