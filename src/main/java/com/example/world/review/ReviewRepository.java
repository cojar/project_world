package com.example.world.review;

import com.example.world.order.ProductOrder;
import com.example.world.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrder(ProductOrder productOrder); // 리뷰부분 제대로 작동하지 않을 시 최우선으로 삭제 고려할 것
    List<Review> findById(Review review);
    Page<Review> findAll(Specification<SiteUser> spec, Pageable pageable);
    Page<Review> findByProductOrder_Product_Id(Long productId, Pageable pageable);

    List<Review> findByAuthor(SiteUser siteUser);

    Page<Review> findByAuthor(SiteUser siteUser, Pageable pageable);

}
