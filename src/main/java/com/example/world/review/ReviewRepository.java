package com.example.world.review;

import com.example.world.product.Product;
import com.example.world.qna.Question;
import com.example.world.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product); // 리뷰부분 제대로 작동하지 않을 시 최우선으로 삭제 고려할 것
    List<Review> findById(Review review);
    Page<Review> findAll(Pageable pageable);
}
