package com.example.world.review;

import com.example.world.order.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrder(ProductOrder productOrder); // 리뷰부분 제대로 작동하지 않을 시 최우선으로 삭제 고려할 것
    List<Review> findById(Review review);
    Page<Review> findAll(Pageable pageable);
}
