package com.example.world.order;

import com.example.world.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    long countByOrderStatus(String orderStatus);
}
